package com.future.pms.core.model.customerdetails

import com.google.gson.annotations.SerializedName

data class Customer(@SerializedName("body") val body: Body, @SerializedName("statusCode")
val statusCode: String, @SerializedName("statusCodeValue") val statusCodeValue: Int)