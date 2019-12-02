package com.future.pms.util

import android.app.NotificationChannel
import android.app.NotificationManager
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
      val format = SimpleDateFormat(FULL_DATE_TIME_FORMAT)
      return format.format(date)
    }

    fun convertLongToTimeOnly(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat(TIME_FORMAT)
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
        if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
          notificationManager.createNotificationChannel(
            NotificationChannel(
              DEFAULT_CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
          )
        }
      }
    }
  }
}