package com.future.pms.ui.superadmin.listuser

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.admin.Admin
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.superadmin.SuperAdminResponse

interface ListUserContract : BaseMVPView {
  fun onFailed(e: String)
  fun loadAllCustomerSuccess(customer: CustomerResponse)
  fun loadAllAdminSuccess(admin: Admin)
  fun loadAllSuperAdminSuccess(superAdmin: SuperAdminResponse)
}