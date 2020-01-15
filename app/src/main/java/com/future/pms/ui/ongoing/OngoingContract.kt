package com.future.pms.ui.ongoing

import com.future.pms.di.base.BaseView
import com.future.pms.model.customerbooking.CustomerBooking

interface OngoingContract : BaseView {
  fun showProgress(show: Boolean)
  fun refreshHome()
  fun loadCustomerOngoingSuccess(ongoing: CustomerBooking)
  fun loadCustomerOngoingFailed(error: String)
  fun checkoutSuccess(idBooking: String)
}