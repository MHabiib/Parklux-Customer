package com.future.pms.ui.profile

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.customerdetail.Customer

interface ProfileContract : BaseMVPView {
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun loadCustomerDetailSuccess(customer: Customer)
  fun onLogout()
}