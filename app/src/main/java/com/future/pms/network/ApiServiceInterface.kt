package com.future.pms.network

import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content
import com.future.pms.model.admin.Admin
import com.future.pms.model.admin.ParkingZoneResponse
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.model.receipt.Receipt
import com.future.pms.model.register.CustomerRequest
import com.future.pms.model.superadmin.SuperAdminResponse
import com.future.pms.model.user.User
import com.future.pms.model.user.UserDetails
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServiceInterface {
 @FormUrlEncoded @POST("oauth/token") fun refresh(@Field("grant_type") grantType: String,
      @Field("refresh_token") refreshAuth: String): Observable<Token>

  //Super admin
  @GET("api3/user/me") fun isSuperAdmin(@Query("access_token")
  accessToken: String?): Observable<String>

  @GET("api3/booking") fun loadAllBooking(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("filter") filter: String?): Observable<Booking>

  @GET("api3/parking-zone") fun loadAllParkingZone(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("name") name: String): Observable<Admin>

  @GET("api3/customer") fun loadAllCustomer(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?, @Query("name") name: String): Observable<CustomerResponse>

  @GET("api3/user") fun loadAllSuperAdmin(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<SuperAdminResponse>

  @POST("api3/user") fun createUser(@Query("access_token") accessToken: String?, @Body
  user: User): Observable<String>

  @PUT("api3/user") fun updateUser(@Query("access_token") accessToken: String?, @Body
  user: User): Observable<String>

  @GET("api3/user/email") fun getEmail(@Query("access_token")
  accessToken: String?): Observable<String>

  @GET("api3/booking/{id}/receipt") fun bookingReceiptSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

  @GET("api3/booking/{id}") fun findBookingById(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Content>

  @POST("api3/booking/{id}/checkout") fun checkoutBookingSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

  @GET("api3/customer/{id}/detail") fun getCustomerDetailSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<Customer>

  @GET("api3/{id}/parking-zone") fun getAdminDetailSA(@Path("id") idBooking: String,
      @Query("access_token") accessToken: String?): Observable<AdminResponse>

  @GET("api3/{id}/user/") fun getUserDetailSA(@Path("id") idBooking: String, @Query("access_token")
  accessToken: String?): Observable<UserDetails>

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
