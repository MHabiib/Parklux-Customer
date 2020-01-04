package com.future.pms.model.superadmin

import com.google.gson.annotations.SerializedName

data class SuperAdminResponse(@SerializedName("body") val body: Body, @SerializedName("statusCode")
val statusCode: String, @SerializedName("statusCodeValue") val statusCodeValue: Int)