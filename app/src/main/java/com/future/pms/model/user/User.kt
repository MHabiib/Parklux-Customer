package com.future.pms.model.user

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("email") val email: String, @SerializedName("password")
val password: String, @SerializedName("role") val role: String)