package com.future.pms.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.util.Constants.Companion.FULL_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.TIME_FORMAT
import java.util.*

class Utils {
  companion object {
    private var DEFAULT_CHANNEL_ID = "pms"
    private var CHANNEL_NAME = "PMS Channel"

    fun convertLongToTime(time: Long): String {
      val date = Date(time)
      val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(FULL_DATE_TIME_FORMAT)
      } else {
        TODO("VERSION.SDK_INT < N")
      }
      return format.format(date)
    }

    fun convertLongToTimeOnly(time: Long): String {
      val date = Date(time)
      val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(TIME_FORMAT)
      } else {
        TODO("VERSION.SDK_INT < N")
      }
      return "${format.format(date)} WIB"
    }

    fun getHistoryParking(list: List<CustomerBooking>): List<CustomerBooking> {
      val historyParking: MutableList<CustomerBooking> = mutableListOf()
      for (customerBookingList in list) {
        if (0L != customerBookingList.dateOut) {
          historyParking.add(customerBookingList)
        }
      }
      return historyParking
    }

    fun createNotificationChannel(notificationManager: NotificationManager) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(DEFAULT_CHANNEL_ID, CHANNEL_NAME, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern =
          longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager.createNotificationChannel(notificationChannel)
      }
    }
  }
}