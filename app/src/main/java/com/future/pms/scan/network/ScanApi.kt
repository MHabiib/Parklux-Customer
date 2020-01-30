package com.future.pms.scan.network

import com.future.pms.core.model.CustomerBooking
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScanApi {
  @POST("api/booking/{id}/{fcm}") fun postCreateBooking(@Path("id") idSlot: String?, @Path("fcm")
  fcm: String?, @Query("access_token")
  accessToken: String?): Observable<CustomerBooking>
}