package com.future.pms.model.receipt

import com.google.gson.annotations.SerializedName

data class Receipt(@SerializedName("dateIn") val dateIn: Long, @SerializedName("dateOut")
val dateOut: Long, @SerializedName("idBooking") val idBooking: String,
    @SerializedName("customerName") val customerName: String, @SerializedName("parkingZoneName")
    val parkingZoneName: String, @SerializedName("address") val address: String,
    @SerializedName("price") val price: Double, @SerializedName("slotName") val slotName: String,
    @SerializedName("totalHours") val totalHours: Int, @SerializedName("totalMinutes")
    val totalMinutes: Int, @SerializedName("totalPrice") val totalPrice: String,
    @SerializedName("status") val status: String)