package com.future.pms.maps.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.future.pms.R
import com.future.pms.databinding.FragmentBottomSheetParkingZoneBinding
import com.future.pms.util.Constants.Companion.PARKING_ZONE_ADDRESS
import com.future.pms.util.Constants.Companion.PARKING_ZONE_IMG
import com.future.pms.util.Constants.Companion.PARKING_ZONE_LATITUDE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_LONGITUDE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_NAME
import com.future.pms.util.Constants.Companion.PARKING_ZONE_OPEN_HOUR
import com.future.pms.util.Constants.Companion.PARKING_ZONE_PHONE
import com.future.pms.util.Constants.Companion.PARKING_ZONE_PRICE
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class ParkingZoneDetailsFragment : BottomSheetDialogFragment() {

  private lateinit var binding: FragmentBottomSheetParkingZoneBinding

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_parking_zone,
        container, false)
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      tvParkingZoneName.text = arguments?.getString(PARKING_ZONE_NAME).toString()
      tvParkingZoneAddress.text = arguments?.getString(PARKING_ZONE_ADDRESS).toString()
      tvParkingZonePhone.text = arguments?.getString(PARKING_ZONE_PHONE).toString()
      val price = arguments?.getDouble(PARKING_ZONE_PRICE).toString()
      pricePerHour.text = String.format(getString(R.string.total_price) + "/hour",
          Utils.thousandSeparator(price.substring(0, price.length - 2).toInt()))
      openHour.text = String.format("Open hours : %s",
          arguments?.getString(PARKING_ZONE_OPEN_HOUR).toString())
      Utils.imageLoader(root, arguments?.getString(PARKING_ZONE_IMG).toString(), ivParkingZone)
      btnOpenGoogleMaps.setOnClickListener {
        val data = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f(%s)",
            arguments?.getDouble(PARKING_ZONE_LATITUDE).toString().toDouble(),
            arguments?.getDouble(PARKING_ZONE_LONGITUDE).toString().toDouble(),
            tvParkingZoneName.text)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
      }
    }
  }
}