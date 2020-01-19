package com.future.pms.ui.superadmin.listuser

import com.future.pms.model.admin.Admin
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.superadmin.SuperAdminResponse
import com.future.pms.ui.base.BaseView

interface ListUserContract : BaseView {
  fun loadAllCustomerSuccess(customer: CustomerResponse)
  fun loadAllAdminSuccess(admin: Admin)
  fun loadAllSuperAdminSuccess(superAdmin: SuperAdminResponse)
}