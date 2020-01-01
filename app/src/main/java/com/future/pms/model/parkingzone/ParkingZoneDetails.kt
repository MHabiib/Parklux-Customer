package com.future.pms.model.parkingzone

import com.google.gson.annotations.SerializedName

data class ParkingZoneDetails(@SerializedName("address") val address: String,
    @SerializedName("emailAdmin") val emailAdmin: String, @SerializedName("idParkingZone")
    val idParkingZone: String, @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("name") val name: String, @SerializedName("openHour") val openHour: String,
    @SerializedName("phoneNumber") val phoneNumber: String, @SerializedName("price")
    val price: Double)