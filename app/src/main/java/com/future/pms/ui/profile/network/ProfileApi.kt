package com.future.pms.ui.profile.network

import com.future.pms.model.customerdetail.Body
import com.future.pms.model.oauth.Token
import com.future.pms.model.register.CustomerRequest
import io.reactivex.Observable
import retrofit2.http.*

interface ProfileApi {
  @GET("api/customer/detail") fun getCustomerDetail(@Query("access_token")
  accessToken: String?): Observable<Body>

  @Multipart @PUT("api/customer/update") fun putUpdateCustomer(@Query("access_token")
  accessToken: String?, @Part("customer") customer: CustomerRequest): Observable<CustomerRequest>

  @FormUrlEncoded @POST("oauth/token") fun refresh(@Field("grant_type") grantType: String,
      @Field("refresh_token") refreshAuth: String): Observable<Token>
}