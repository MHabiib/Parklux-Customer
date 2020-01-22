package com.future.pms.ui.superadmin.userdetails.network

import com.future.pms.model.admin.ParkingZoneResponse
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.register.CustomerRequest
import com.future.pms.model.user.User
import com.future.pms.model.user.UserDetails
import io.reactivex.Observable
import retrofit2.http.*

interface UserDetailsApi {
  @GET("api3/customer/{id}/detail") fun getCustomerDetailSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Customer>

  @GET("api3/{id}/user/") fun getUserDetailSA(@Path("id") idBooking: String, @Query("access_token")
  accessToken: String?): Observable<UserDetails>

  @GET("api3/{id}/parking-zone") fun getAdminDetailSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<AdminResponse>

  @Multipart @PUT("api3/customer/{id}/update") fun updateCustomer(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?, @Part("customer")
      customer: CustomerRequest): Observable<CustomerRequest>

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