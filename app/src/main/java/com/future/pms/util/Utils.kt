package com.future.pms.util

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.future.pms.R
import com.future.pms.util.Constants.Companion.FULL_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.SHORT_MONTH_DATE_TIME_FORMAT
import com.future.pms.util.Constants.Companion.TIME_FORMAT
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {
  companion object {

    fun convertLongToTime(time: Long): String {
      return SimpleDateFormat(FULL_DATE_TIME_FORMAT, Locale.getDefault()).format(Date(time))
    }

    fun convertLongToTimeOnly(time: Long): String {
      return "${SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(Date(time))} WIB"
    }

    fun convertLongToTimeShortMonth(time: Long): String {
      return SimpleDateFormat(SHORT_MONTH_DATE_TIME_FORMAT, Locale.getDefault()).format(Date(time))
    }

    fun thousandSeparator(int: Int): String {
      return DecimalFormat("#,###,###").format(int)
    }

    fun imageLoader(view: View, imageName: String, imageView: ImageView) {
      Glide.with(view).load(imageName).transform(CenterCrop(), RoundedCorners(80)).apply(
          RequestOptions().signature(ObjectKey(imageName))).placeholder(
          R.drawable.ic_parking_zone_default).error(R.drawable.ic_parking_zone_default).fallback(
          R.drawable.ic_parking_zone_default).into(imageView)
    }
  }
}