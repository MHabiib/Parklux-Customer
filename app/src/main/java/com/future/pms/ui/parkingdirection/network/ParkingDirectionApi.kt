package com.future.pms.ui.parkingdirection.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ParkingDirectionApi {
  @GET("api/parking-zone/{idBooking}/parking-layout") fun getParkingLayout(@Path("idBooking")
  idBooking: String, @Query("access_token") accessToken: String?): Observable<String>
}