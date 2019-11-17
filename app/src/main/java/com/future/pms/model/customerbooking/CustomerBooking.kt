package com.future.pms.model.customerbooking

data class CustomerBooking(
    val dateIn: Long,
    val dateOut: Long,
    val idBooking: String,
    val idParkingZone: String,
    val idSlot: String,
    val idUser: String,
    val parkingZoneName: String,
    val price: Double,
    val slotName: String, val totalTime: String, val imageUrl: String
)