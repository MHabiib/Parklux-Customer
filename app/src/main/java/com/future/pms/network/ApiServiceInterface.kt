package com.future.pms.network

import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.customerbooking.CustomerBooking
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
}
