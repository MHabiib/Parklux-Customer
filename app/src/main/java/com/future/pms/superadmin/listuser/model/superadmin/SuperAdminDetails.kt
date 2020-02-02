package com.future.pms.superadmin.listuser.model.superadmin

import com.google.gson.annotations.SerializedName

data class SuperAdminDetails(@SerializedName("email") val email: String, @SerializedName("idUser")
val idUser: String, @SerializedName("role") val role: String, var position: Int)