package com.future.pms.ui.superadmin.listactivity

import com.future.pms.di.base.BaseView
import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content

interface ListActivityContract : BaseView {
  fun loadAllBookingSuccess(booking: Booking)
  fun findBookingByIdSuccess(booking: Content)
}