package com.future.pms.network

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.receipt.Receipt
import io.reactivex.Observable
import retrofit2.http.*

interface ApiServiceInterface {
    @GET("api/user/customer/detail")
    fun getCustomerDetail(@Query("access_token") access_token: String?): Observable<Customer>

    @GET("api/booking/customer")
    fun getCustomerBooking(@Query("access_token") access_token: String?): Observable<List<CustomerBooking>>

    @GET("api/booking/{id}/receipt")
    fun getBookingReceipt(@Path("id") idReceipt: String, @Query("access_token") access_token: String?): Observable<Receipt>

    @GET("api/booking/customer/ongoing") fun getOngoingBooking(@Query("access_token")
    access_token: String?): Observable<CustomerBooking>

    @POST("api/booking")
    fun postCreateBooking(
        @Body idSlot: String?, @Query("access_token")
        access_token: String?
    ): Observable<String>

    @POST("api/booking/checkout")
    fun postBookingCheckout(
        @Query("access_token")
        access_token: String?
    ): Observable<String>
}
