package com.future.pms.ui.home

import com.future.pms.di.base.BaseView
import com.future.pms.model.customerdetail.Body

interface HomeContract : BaseView {
  fun loadCustomerDetailSuccess(customer: Body)
  fun getDateNow()
  fun showOngoingFragment()
  fun showHistoryFragment()
}