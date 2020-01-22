package com.future.pms.ui.superadmin.listactivity.view

import com.future.pms.model.activity.Booking
import com.future.pms.model.activity.Content
import com.future.pms.ui.base.BaseView

interface ListActivityContract : BaseView {
  fun loadAllBookingSuccess(booking: Booking)
  fun findBookingByIdSuccess(booking: Content)
}