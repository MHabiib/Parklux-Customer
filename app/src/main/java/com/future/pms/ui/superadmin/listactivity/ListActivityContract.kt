package com.future.pms.ui.superadmin.listactivity

import com.future.pms.model.activity.Booking
import com.future.pms.di.base.BaseMVPView

interface ListActivityContract : BaseMVPView {
  fun onFailed(e: String)
  fun loadAllBookingSuccess(booking: Booking)
}