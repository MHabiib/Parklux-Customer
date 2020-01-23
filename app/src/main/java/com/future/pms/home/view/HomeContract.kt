package com.future.pms.home.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.customerdetails.Body

interface HomeContract : BaseView {
  fun loadCustomerDetailSuccess(customer: Body)
  fun getDateNow()
  fun showOngoingFragment()
  fun showHistoryFragment()
}