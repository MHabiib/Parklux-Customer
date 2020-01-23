package com.future.pms.home.network

import com.future.pms.core.model.customerdetails.Body
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>
}