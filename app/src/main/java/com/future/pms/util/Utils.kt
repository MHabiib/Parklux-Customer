package com.future.pms.util

import android.icu.text.SimpleDateFormat
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.util.Constants.Companion.FULL_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.TIME_FORMAT
import java.util.*

class Utils {
  companion object {
    fun convertLongToTime(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat(FULL_DATE_TIME_FORMAT)
      return format.format(date)
    }

    fun convertLongToTimeOnly(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat(TIME_FORMAT)
      return String.format("%s WIB", format.format(date))
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
  }
}