package com.future.pms.superadmin.homesuperadmin.network

import com.future.pms.superadmin.userdetails.model.User
import io.reactivex.Observable
import retrofit2.http.*

interface HomeApiSuperAdmin {
  @POST("api3/user") fun createUser(@Query("access_token") accessToken: String?, @Body
  user: User): Observable<String>

  @PUT("api3/user") fun updateUser(@Query("access_token") accessToken: String?, @Body
  user: User): Observable<String>

  @GET("api3/user/email") fun getEmail(@Query("access_token")
  accessToken: String?): Observable<String>

}