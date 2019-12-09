package com.future.pms.ui.bookingdetail

import com.future.pms.model.customerbooking.CustomerBooking

interface BookingDetailContract {
  fun getLayoutSuccess(slotsLayout: String)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun loadBookingSuccess(booking: CustomerBooking)
}