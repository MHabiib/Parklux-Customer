package com.future.pms.ui.superadmin.listcustomer

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.customer.CustomerResponse

interface ListCustomerContract : BaseMVPView {
  fun onFailed(e: String)
  fun loadAllCustomerSuccess(customer: CustomerResponse)
}