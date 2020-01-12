package com.future.pms.ui.ongoing

import com.future.pms.model.customerbooking.CustomerBooking

interface OngoingContract {
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun refreshHome()
  fun loadCustomerOngoingSuccess(ongoing: CustomerBooking)
  fun loadCustomerOngoingFailed(error: String)
  fun checkoutSuccess(idBooking: String)
}