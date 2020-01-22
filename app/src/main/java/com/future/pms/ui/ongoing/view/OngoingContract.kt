package com.future.pms.ui.ongoing.view

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.ui.base.BaseView

interface OngoingContract : BaseView {
  fun showProgress(show: Boolean)
  fun refreshHome()
  fun loadCustomerOngoingSuccess(ongoing: CustomerBooking)
  fun loadCustomerOngoingFailed(error: String)
  fun checkoutSuccess(idBooking: String)
}