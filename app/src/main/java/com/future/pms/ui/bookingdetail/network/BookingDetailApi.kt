package com.future.pms.ui.bookingdetail.network

import com.future.pms.model.customerbooking.CustomerBooking
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookingDetailApi {
  @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @GET("api/parking-zone/{idBooking}/parking-layout") fun getParkingLayout(@Path("idBooking")
  idBooking: String, @Query("access_token") accessToken: String?): Observable<String>
}