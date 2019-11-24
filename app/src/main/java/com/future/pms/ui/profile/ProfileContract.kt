package com.future.pms.ui.profile

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.customerdetail.Customer

interface ProfileContract : BaseMVPView {
  fun onSuccess()
  fun onFailed(e: String)
  fun onError(e: Throwable)
  fun showProgress(show: Boolean)
  fun showErrorMessage(error: String)
  fun unauthorized()
  fun loadCustomerDetailSuccess(customer: Customer)
  fun onLogout()
}