package com.future.pms.network

import com.future.pms.model.oauth.Token
import com.future.pms.model.oauth.request.Auth
import com.future.pms.model.oauth.request.Refresh
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthAPI {

    @POST("oauth/token")
    fun auth(@Body auth: Auth): Call<Token>

    @POST("refresh")
    fun refresh(@Body refreshAuth: Refresh): Call<Token>
}