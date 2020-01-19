package com.future.pms.ui.profile

import com.future.pms.model.customerdetail.Body
import com.future.pms.ui.base.BaseView

interface ProfileContract : BaseView {
  fun onSuccess()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(customer: Body)
  fun onLogout()
}