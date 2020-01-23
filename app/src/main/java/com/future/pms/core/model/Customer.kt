package com.future.pms.core.model

import com.google.gson.annotations.SerializedName

data class Customer(@SerializedName("email") val email: String, @SerializedName("name")
val name: String, @SerializedName("password") val password: String, @SerializedName("phoneNumber")
val phoneNumber: String)