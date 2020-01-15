package com.future.pms.ui.profile

import com.future.pms.di.base.BaseView
import com.future.pms.model.customerdetail.Customer

interface ProfileContract : BaseView {
  fun onSuccess()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(customer: Customer)
  fun onLogout()
}