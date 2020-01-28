package com.future.pms.scan.network

import com.future.pms.core.model.CustomerBooking
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ScanApi {
  @POST("api/booking") fun postCreateBooking(@Body idSlot: String?, @Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

}