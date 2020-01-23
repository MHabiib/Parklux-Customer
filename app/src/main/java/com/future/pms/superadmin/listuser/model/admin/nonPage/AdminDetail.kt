package com.future.pms.superadmin.listuser.model.admin.nonPage

import com.google.gson.annotations.SerializedName

data class AdminDetail(@SerializedName("address") val address: String, @SerializedName("emailAdmin")
val emailAdmin: String, @SerializedName("idParkingZone") val idParkingZone: String,
    @SerializedName("imageUrl") val imageUrl: String, @SerializedName("name") val name: String,
    @SerializedName("openHour") val openHour: String, @SerializedName("phoneNumber")
    val phoneNumber: String, @SerializedName("price") val price: Double)