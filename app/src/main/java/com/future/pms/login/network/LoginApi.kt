package com.future.pms.login.network

import com.future.pms.core.model.Token
import com.future.pms.core.model.customerdetails.Body
import io.reactivex.Observable
import retrofit2.http.*

interface LoginApi {
  @FormUrlEncoded @POST("oauth/token") fun auth(@Field("username") username: String,
      @Field("password") password: String, @Field("grant_type")
      grantType: String): Observable<Token>

  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>
}