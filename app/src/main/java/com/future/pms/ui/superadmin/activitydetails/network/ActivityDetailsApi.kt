package com.future.pms.ui.superadmin.activitydetails.network

import com.future.pms.model.receipt.Receipt
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityDetailsApi {
  @GET("api3/booking/{id}/receipt") fun bookingReceiptSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

  @POST("api3/booking/{id}/checkout") fun checkoutBookingSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

}