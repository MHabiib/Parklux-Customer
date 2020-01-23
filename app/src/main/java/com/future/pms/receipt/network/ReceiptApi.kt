package com.future.pms.receipt.network

import com.future.pms.core.model.Receipt
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReceiptApi {
  @GET("api/booking/{id}/receipt") fun getBookingReceipt(@Path("id") idReceipt: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

}