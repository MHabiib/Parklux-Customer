package com.future.pms.ui.splash.network

import com.future.pms.model.oauth.Token
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SplashApi {
  @FormUrlEncoded @POST("oauth/token") fun refresh(@Field("grant_type") grantType: String,
      @Field("refresh_token") refreshAuth: String): Observable<Token>

}