package com.future.pms.profile.network

import com.future.pms.core.model.Customer
import com.future.pms.core.model.customerdetails.Body
import io.reactivex.Observable
import retrofit2.http.*

interface ProfileApi {
  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>

  @Multipart @PUT("api/customer/update") fun putUpdateCustomer(@Query("access_token")
  accessToken: String?, @Part("customer") customer: Customer): Observable<Customer>

}