package com.future.pms.ui.login.network

import com.future.pms.model.customerdetail.Body
import com.future.pms.model.oauth.Token
import io.reactivex.Observable
import retrofit2.http.*

interface LoginApi {
  @FormUrlEncoded @POST("oauth/token") fun auth(@Field("username") username: String,
      @Field("password") password: String, @Field("grant_type")
      grantType: String): Observable<Token>

  @FormUrlEncoded @POST("oauth/token") fun refresh(@Field("grant_type") grantType: String,
      @Field("refresh_token") refreshAuth: String): Observable<Token>

  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>
}