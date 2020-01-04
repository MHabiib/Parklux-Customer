package com.future.pms.model.customer

import com.google.gson.annotations.SerializedName

data class CustomerDetails(@SerializedName("email") val email: String, @SerializedName("idCustomer")
val idCustomer: String, @SerializedName("name") val name: String, @SerializedName("phoneNumber")
val phoneNumber: String, var position: Int)