package com.future.pms.maps.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseActivity
import com.future.pms.core.model.Token
import com.future.pms.maps.injection.DaggerMapsComponent
import com.future.pms.maps.injection.MapsComponent
import com.future.pms.maps.model.ParkingZoneLatLng
import com.future.pms.maps.presenter.MapsPresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.MAPS_ACTIVITY
import com.future.pms.util.Constants.Companion.PARKING_ZONE_ADDRESS
import com.future.pms.util.Constants.Companion.PARKING_ZONE_IMG
import com.future.pms.util.Constants.Companion.PARKING_ZONE_LATITUDE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_LONGITUDE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_NAME
import com.future.pms.util.Constants.Companion.PARKING_ZONE_OPEN_HOUR
import com.future.pms.util.Constants.Companion.PARKING_ZONE_PHONE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_PRICE
import com.future.pms.util.Constants.Companion.REQUEST_LOCATION_PERMISSION
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_maps.*
import javax.inject.Inject

class MapsActivity : BaseActivity(), OnMapReadyCallback, MapsContract {
  private var daggerBuild: MapsComponent = DaggerMapsComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: MapsPresenter
  @Inject lateinit var gson: Gson
  private lateinit var map: GoogleMap
  private var homeLatLng = LatLng(-6.175392, 106.827153)
  private val bottomSheetFragment = ParkingZoneDetailsFragment()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_maps)
    presenter.attach(this)
    ibBack.setOnClickListener {
      onBackPressed()
    }
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    enableMyLocation()
    val zoomLevel = 15f
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
    map.uiSettings.isRotateGesturesEnabled = false
    setMapStyle(map)

    presenter.loadLocation(gson.fromJson(
        this.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken)
  }

  override fun loadLocationSuccess(list: List<ParkingZoneLatLng>) {
    for (parkingZone in list) {
      val snippet = String.format(
          "{\"name\":\"%s\",\"price\":%f,\"openHour\":\"%s\",\"address\":\"%s\",\"phoneNumber\":\"%s\",\"latitude\":%f,\"longitude\":%f,\"imageUrl\":\"%s\"}",
          parkingZone.name, parkingZone.price, parkingZone.openHour, parkingZone.address,
          parkingZone.phoneNumber, parkingZone.latitude, parkingZone.longitude,
          parkingZone.imageUrl)
      map.addMarker(
          MarkerOptions().position(LatLng(parkingZone.latitude, parkingZone.longitude)).title(
              parkingZone.name).snippet(snippet).icon(
              BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
      map.setOnMarkerClickListener { marker ->
        if (!marker.isInfoWindowShown) {
          val parkingZoneLatLng = gson.fromJson(marker.snippet, ParkingZoneLatLng::class.java)
          val bundle = Bundle()
          bundle.putString(PARKING_ZONE_NAME, parkingZoneLatLng.name)
          bundle.putString(PARKING_ZONE_ADDRESS, parkingZoneLatLng.address)
          bundle.putString(PARKING_ZONE_PHONE, parkingZoneLatLng.phoneNumber)
          bundle.putDouble(PARKING_ZONE_PRICE, parkingZoneLatLng.price)
          bundle.putString(PARKING_ZONE_OPEN_HOUR, parkingZoneLatLng.openHour)
          bundle.putString(PARKING_ZONE_IMG, parkingZoneLatLng.imageUrl)
          bundle.putDouble(PARKING_ZONE_LONGITUDE, parkingZoneLatLng.longitude)
          bundle.putDouble(PARKING_ZONE_LATITUDE, parkingZoneLatLng.latitude)
          bottomSheetFragment.arguments = bundle
          if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
          }
        }
        true
      }
      map.uiSettings.isMapToolbarEnabled = true
    }
  }

  private fun setMapStyle(map: GoogleMap) {
    try {
      if (!map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))) {
        Log.e(MAPS_ACTIVITY, "Style parsing failed.")
      }
    } catch (e: Resources.NotFoundException) {
      Log.e(MAPS_ACTIVITY, "Can't find style. Error: ", e)
    }
  }

  private fun isPermissionGranted(): Boolean {
    return ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
  }

  @SuppressLint("MissingPermission") private fun enableMyLocation() {
    if (isPermissionGranted()) {
      map.isMyLocationEnabled = true
      val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
      val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
      if (location != null) {
        homeLatLng = LatLng(location.latitude, location.longitude)
      }
    } else {
      ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
          REQUEST_LOCATION_PERMISSION)
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {
    if (requestCode == REQUEST_LOCATION_PERMISSION) {
      if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
        enableMyLocation()
      }
    }
  }

  override fun onFailed(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}
