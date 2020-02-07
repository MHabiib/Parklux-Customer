package com.future.pms.maps.model

import com.google.gson.annotations.SerializedName

data class ParkingZoneLatLng(@SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: Double, @SerializedName("longitude")
    val longitude: Double, @SerializedName("name") val name: String, @SerializedName("openHour")
    val openHour: String, @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("imageUrl") val imageUrl: String, @SerializedName("price") val price: Double)