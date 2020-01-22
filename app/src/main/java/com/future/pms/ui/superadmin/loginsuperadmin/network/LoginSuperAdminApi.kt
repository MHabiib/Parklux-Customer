package com.future.pms.ui.superadmin.loginsuperadmin.network

import com.future.pms.model.oauth.Token
import io.reactivex.Observable
import retrofit2.http.*

interface LoginSuperAdminApi {
  @FormUrlEncoded @POST("oauth/token") fun auth(@Field("username") username: String,
      @Field("password") password: String, @Field("grant_type")
      grantType: String): Observable<Token>

  @GET("api3/user/me") fun isSuperAdmin(@Query("access_token")
  accessToken: String?): Observable<String>
}