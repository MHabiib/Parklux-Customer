package com.future.pms.ui.ongoing.network

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.receipt.Receipt
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OngoingApi {
  @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @POST("api/booking/checkout") fun postBookingCheckout(@Query("access_token")
  accessToken: String?): Observable<Receipt>
}