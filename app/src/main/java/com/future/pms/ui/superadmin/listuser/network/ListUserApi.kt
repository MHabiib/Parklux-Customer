package com.future.pms.ui.superadmin.listuser.network

import com.future.pms.model.admin.Admin
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.superadmin.SuperAdminResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ListUserApi {
  @GET("api3/customer") fun loadAllCustomer(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("name") name: String): Observable<CustomerResponse>

  @GET("api3/user") fun loadAllSuperAdmin(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<SuperAdminResponse>

  @GET("api3/parking-zone") fun loadAllParkingZone(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("name") name: String): Observable<Admin>
}