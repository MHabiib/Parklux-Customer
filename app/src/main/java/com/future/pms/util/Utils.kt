package com.future.pms.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.R
import com.future.pms.network.NetworkConstant
import com.future.pms.util.Constants.Companion.FULL_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.SHORT_MONTH_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.TIME_FORMAT
import java.text.DecimalFormat
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

    fun convertLongToTimeShortMonth(time: Long): String {
      val date = Date(time)
      val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        SimpleDateFormat(SHORT_MONTH_DATE_TIME_FORMAT, Locale.ENGLISH)
      } else {
        TODO("VERSION.SDK_INT < N")
      }
      return format.format(date)
    }

    fun thousandSeparator(int: Int): String {
      return DecimalFormat("#,###,###").format(int)
    }

    fun createNotificationChannel(notificationManager: NotificationManager) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val notificationChannel = NotificationChannel(DEFAULT_CHANNEL_ID, CHANNEL_NAME, importance)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200,
            400)
        notificationManager.createNotificationChannel(notificationChannel)
      }
    }

    fun imageLoader(viewGroup: ViewGroup, imageName: String, imageView: ImageView) {
      Glide.with(viewGroup).load(NetworkConstant.BASE + "img/" + imageName).transform(CenterCrop(),
          RoundedCorners(80)).placeholder(R.drawable.ic_parking_zone_default).error(
          R.drawable.ic_parking_zone_default).fallback(R.drawable.ic_parking_zone_default).into(
          imageView)
    }

    fun imageLoaderView(view: View, imageName: String, imageView: ImageView) {
      Glide.with(view).load(NetworkConstant.BASE + "img/" + imageName).transform(CenterCrop(),
          RoundedCorners(80)).placeholder(R.drawable.ic_parking_zone_default).error(
          R.drawable.ic_parking_zone_default).fallback(R.drawable.ic_parking_zone_default).into(
          imageView)
    }
  }
}