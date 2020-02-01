package com.future.pms.register.network

import com.future.pms.core.model.Customer
import com.future.pms.core.model.Token
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterApi {
  @POST("customer/create") fun postCreateCustomer(@Body customer: Customer): Observable<String>

  @FormUrlEncoded @POST("oauth/token") fun auth(@Field("username") username: String,
      @Field("password") password: String, @Field("grant_type")
      grantType: String): Observable<Token>
}