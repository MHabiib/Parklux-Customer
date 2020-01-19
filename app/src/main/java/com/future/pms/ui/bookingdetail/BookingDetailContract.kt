package com.future.pms.ui.bookingdetail

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.ui.base.BaseView

interface BookingDetailContract : BaseView {
  fun getLayoutSuccess(slotsLayout: String)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun loadBookingSuccess(booking: CustomerBooking)
}