package com.future.pms.model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(@SerializedName("body") val user: UserDetails, @SerializedName("statusCode")
val statusCode: String, @SerializedName("statusCodeValue") val statusCodeValue: Int)