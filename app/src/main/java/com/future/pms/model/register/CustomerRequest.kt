package com.future.pms.model.register

import com.google.gson.annotations.SerializedName

data class CustomerRequest(
  @SerializedName("email") val email: String, @SerializedName("name") val name: String,
  @SerializedName("password") val password: String, @SerializedName("phoneNumber")
  val phoneNumber: String
)