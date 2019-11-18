package com.future.pms.util

import android.icu.text.SimpleDateFormat
import com.future.pms.model.customerbooking.CustomerBooking
import java.util.*

class Utils {
  companion object {
    fun convertLongToTime(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat("HH:mm - dd MMMM yyyy ")
      return format.format(date)
    }

    fun convertLongToTimeShortMonth(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat("HH:mm dd MMM yyyy ")
      return format.format(date)
    }

    fun convertLongToTimeOnly(time: Long): String {
      val date = Date(time)
      val format = SimpleDateFormat("HH:mm")
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