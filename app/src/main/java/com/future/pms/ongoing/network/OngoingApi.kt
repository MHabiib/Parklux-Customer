package com.future.pms.ongoing.network

import com.future.pms.core.model.CustomerBooking
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OngoingApi {
  @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @POST("api/booking/checkout") fun postBookingCheckout(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>
}