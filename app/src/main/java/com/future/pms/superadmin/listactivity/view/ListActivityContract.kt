package com.future.pms.superadmin.listactivity.view

import com.future.pms.core.base.BaseView
import com.future.pms.superadmin.activitydetails.model.Booking
import com.future.pms.superadmin.activitydetails.model.Content

interface ListActivityContract : BaseView {
  fun loadAllBookingSuccess(booking: Booking)
  fun findBookingByIdSuccess(booking: Content)
}