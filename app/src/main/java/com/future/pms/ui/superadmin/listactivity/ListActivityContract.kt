package com.future.pms.ui.superadmin.listactivity

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content

interface ListActivityContract : BaseMVPView {
  fun onFailed(e: String)
  fun loadAllBookingSuccess(booking: Booking)
  fun findBookingByIdSuccess(booking: Content)
}