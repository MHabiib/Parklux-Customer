package com.future.pms.network

import com.future.pms.model.oauth.Token
import retrofit2.Call
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

    @FormUrlEncoded
    @POST("auth/token")
    fun refresh(
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") refreshAuth: String
    ): Call<Token>
}