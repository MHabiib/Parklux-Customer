package com.future.pms.model.user

import com.google.gson.annotations.SerializedName

data class UserDetails(@SerializedName("email") val email: String, @SerializedName("idUser")
val idUser: String?, @SerializedName("password") val password: String, @SerializedName("role")
val role: String)