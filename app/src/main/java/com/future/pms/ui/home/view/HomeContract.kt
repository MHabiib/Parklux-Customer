package com.future.pms.ui.home.view

import com.future.pms.model.customerdetail.Body
import com.future.pms.ui.base.BaseView

interface HomeContract : BaseView {
  fun loadCustomerDetailSuccess(customer: Body)
  fun getDateNow()
  fun showOngoingFragment()
  fun showHistoryFragment()
}