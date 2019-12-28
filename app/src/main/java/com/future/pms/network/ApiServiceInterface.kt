package com.future.pms.network

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.history.History
import com.future.pms.model.receipt.Receipt
import com.future.pms.model.register.CustomerRequest
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
}
