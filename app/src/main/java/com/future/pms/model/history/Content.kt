package com.future.pms.model.history

import com.google.gson.annotations.SerializedName

data class Content(@SerializedName("address") val address: Any, @SerializedName("dateIn")
val dateIn: Long, @SerializedName("dateOut") val dateOut: Long, @SerializedName("idBooking")
val idBooking: String, @SerializedName("idParkingZone") val idParkingZone: String,
    @SerializedName("idSlot") val idSlot: String, @SerializedName("idUser") val idUser: String,
    @SerializedName("imageUrl") val imageUrl: String, @SerializedName("parkingZoneName")
    val parkingZoneName: String, @SerializedName("price") val price: Double,
    @SerializedName("slotName") val slotName: String, @SerializedName("totalTime")
    val totalTime: String)