package com.future.pms.ongoing.network

import com.future.pms.core.model.CustomerBooking
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OngoingApi {
  @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @POST("api/booking/checkoutStepOne/{fcmToken}")
  fun postBookingCheckout(
    @Path("fcmToken") fcm: String, @Query("access_token")
    accessToken: String?
  ): Observable<ResponseBody>
}