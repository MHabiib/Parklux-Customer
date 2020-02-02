package com.future.pms.superadmin.listuser.model.customer

import com.google.gson.annotations.SerializedName

data class CustomerResponse(@SerializedName("body") val body: Body, @SerializedName("statusCode")
val statusCode: String, @SerializedName("statusCodeValue") val statusCodeValue: Int)