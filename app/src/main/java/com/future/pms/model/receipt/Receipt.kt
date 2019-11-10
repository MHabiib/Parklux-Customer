package com.future.pms.model.receipt

import java.net.Inet4Address

data class Receipt(
    val dateIn: Long,
    val dateOut: Long,
    val idBooking: String,
    val parkingZoneName: String,
    val address: String,
    val price: Double,
    val slotName: String,
    val totalHours: Int,
    val totalMinutes: Int,
    val totalPrice: String
)