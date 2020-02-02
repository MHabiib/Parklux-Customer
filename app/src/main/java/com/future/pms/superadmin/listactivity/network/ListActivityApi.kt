package com.future.pms.superadmin.listactivity.network

import com.future.pms.superadmin.activitydetails.model.Booking
import com.future.pms.superadmin.activitydetails.model.Content
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