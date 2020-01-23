package com.future.pms.superadmin.userdetails.network

import com.future.pms.core.model.Customer
import com.future.pms.superadmin.listuser.model.admin.ParkingZoneResponse
import com.future.pms.superadmin.listuser.model.admin.nonPage.AdminResponse
import com.future.pms.superadmin.userdetails.model.User
import com.future.pms.superadmin.userdetails.model.UserDetails
import io.reactivex.Observable
import retrofit2.http.*

interface UserDetailsApi {
  @GET("api3/customer/{id}/detail") fun getCustomerDetailSA(@Path("id") idBooking: String,
      @Query("access_token")
      accessToken: String?): Observable<com.future.pms.core.model.customerdetails.Customer>

  @GET("api3/{id}/user/") fun getUserDetailSA(@Path("id") idBooking: String, @Query("access_token")
  accessToken: String?): Observable<UserDetails>

  @GET("api3/{id}/parking-zone") fun getAdminDetailSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<AdminResponse>

  @Multipart @PUT("api3/customer/{id}/update") fun updateCustomer(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?, @Part("customer")
      customer: Customer): Observable<Customer>

  @Multipart @PUT("api3/parking-zone/{id}/update-zone") fun updateAdmin(@Path("id")
  idBooking: String, @Query("access_token") accessToken: String?, @Part("parkingZone")
  parkingZone: ParkingZoneResponse): Observable<ParkingZoneResponse>

  @PUT("api3/{id}/user") fun updateUserFromList(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?, @Body user: User): Observable<String>

  @POST("api3/{id}/customer/ban/") fun banCustomer(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<String>

  @DELETE("api3/{id}/user/") fun deleteSuperAdmin(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<String>
}