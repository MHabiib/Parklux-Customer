package com.future.pms.util

import android.icu.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("HH:mm dd MMMM yyyy ")
            return format.format(date)
        }

        fun convertLongToTimeShortMonth(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("HH:mm dd MMM yyyy ")
            return format.format(date)
        }
    }
}