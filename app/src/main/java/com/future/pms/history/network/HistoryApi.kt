package com.future.pms.history.network

import com.future.pms.history.model.History
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryApi {
  @GET("api/booking/customer") fun getCustomerBooking(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<History>
}