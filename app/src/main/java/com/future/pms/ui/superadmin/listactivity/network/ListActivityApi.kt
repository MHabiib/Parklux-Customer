package com.future.pms.ui.superadmin.listactivity.network

import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ListActivityApi {
  @GET("api3/booking") fun loadAllBooking(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("filter") filter: String?): Observable<Booking>

  @GET("api3/booking/{id}") fun findBookingById(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Content>

}