package com.future.pms.bookingdetail.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.CustomerBooking

interface BookingDetailContract : BaseView {
  fun getLayoutSuccess(slotsLayout: String)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun loadBookingSuccess(booking: CustomerBooking)
}