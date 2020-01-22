package com.future.pms.ui.register.network

import com.future.pms.model.register.CustomerRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
  @POST("customer/create") fun postCreateCustomer(@Body
  customerRequest: CustomerRequest): Observable<String>

}