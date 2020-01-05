package com.future.pms.network

import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content
import com.future.pms.model.admin.Admin
import com.future.pms.model.admin.ParkingZoneResponse
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.history.History
import com.future.pms.model.receipt.Receipt
import com.future.pms.model.register.CustomerRequest
import com.future.pms.model.superadmin.SuperAdminResponse
import com.future.pms.model.user.User
import com.future.pms.model.user.UserResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServiceInterface {
  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Customer>

  @GET("api/booking/customer") fun getCustomerBooking(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<History>

  @GET("api/booking/{id}/receipt") fun getBookingReceipt(@Path("id") idReceipt: String,
      @Query("access_token") accessToken: String?): Observable<Receipt>

  @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @POST("api/booking") fun postCreateBooking(@Body idSlot: String?, @Query("access_token")
  accessToken: String?): Observable<CustomerBooking>

  @POST("api/booking/checkout") fun postBookingCheckout(@Query("access_token")
  accessToken: String?): Observable<Receipt>

  @POST("customer/create") fun postCreateCustomer(@Body
  customerReques: CustomerRequest): Observable<String>

  @Multipart @PUT("api/customer/update") fun putUpdateCustomer(@Query("access_token")
  accessToken: String?, @Part("customer") customer: CustomerRequest): Observable<CustomerRequest>

  @GET("api/parking-zone/{idBooking}/parking-layout") fun getParkingLayout(@Path("idBooking")
  idBooking: String, @Query("access_token") accessToken: String?): Observable<String>

  //Super admin
  @GET("api3/user/me") fun isSuperAdmin(@Query("access_token")
  accessToken: String?): Observable<String>

  @GET("api3/booking") fun loadAllBooking(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<Booking>

  @GET("api3/parking-zone") fun loadAllParkingZone(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<Admin>

  @GET("api3/customer") fun loadAllCustomer(@Query("access_token") accessToken: String?,
      @Query("page") page: Int?): Observable<CustomerResponse>

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
  accessToken: String?): Observable<UserResponse>

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
