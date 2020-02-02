package com.future.pms.profile.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.customerdetails.Body

interface ProfileContract : BaseView {
  fun onSuccess()
  fun showProgress(show: Boolean)
  fun loadCustomerDetailSuccess(customer: Body)
  fun onLogout()
}