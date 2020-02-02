package com.future.pms.maps.network

import com.future.pms.maps.model.ParkingZoneLatLng
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {
  @GET("api/parking-zone/latlng") fun getLatLng(@Query("access_token")
  accessToken: String?): Observable<List<ParkingZoneLatLng>>
}