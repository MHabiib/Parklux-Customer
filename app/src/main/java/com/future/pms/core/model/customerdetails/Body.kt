package com.future.pms.core.model.customerdetails

import com.google.gson.annotations.SerializedName

data class Body(@SerializedName("email") val email: String, @SerializedName("idCustomer")
val idCustomer: String, @SerializedName("name") val name: String, @SerializedName("phoneNumber")
val phoneNumber: String)