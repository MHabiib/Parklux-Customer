package com.future.pms.ui.home.network

import com.future.pms.model.customerdetail.Body
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface IHomeApi {
  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>

}