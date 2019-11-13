package com.future.pms.network

import com.future.pms.model.oauth.Token
import com.future.pms.model.oauth.request.Refresh
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthAPI {
    @FormUrlEncoded
    @POST("oauth/token")
    fun auth(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grant_type: String
    ): Call<Token>

    @POST("refresh")
    fun refresh(@Body refreshAuth: Refresh): Call<Token>
}