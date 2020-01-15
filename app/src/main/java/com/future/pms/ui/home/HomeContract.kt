package com.future.pms.ui.home

import com.future.pms.di.base.BaseView
import com.future.pms.model.customerdetail.Customer

interface HomeContract : BaseView {
  fun loadCustomerDetailSuccess(customer: Customer)
  fun getDateNow()
  fun showOngoingFragment()
  fun showHistoryFragment()
}