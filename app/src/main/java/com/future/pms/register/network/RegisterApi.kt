package com.future.pms.register.network

import com.future.pms.core.model.Customer
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
  @POST("customer/create") fun postCreateCustomer(@Body customer: Customer): Observable<String>

}