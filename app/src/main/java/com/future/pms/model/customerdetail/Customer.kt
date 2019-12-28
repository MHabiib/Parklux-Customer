package com.future.pms.model.customerdetail

import com.google.gson.annotations.SerializedName

data class Customer(@SerializedName("body") val body: Body, @SerializedName("headers")
val headers: Headers, @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusCodeValue") val statusCodeValue: Int)