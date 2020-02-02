package com.future.pms.ongoing.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.CustomerBooking

interface OngoingContract : BaseView {
  fun showProgress(show: Boolean)
  fun refreshHome()
  fun loadCustomerOngoingSuccess(ongoing: CustomerBooking)
  fun loadCustomerOngoingFailed(error: String)
  fun checkoutSuccess(idBooking: String)
}