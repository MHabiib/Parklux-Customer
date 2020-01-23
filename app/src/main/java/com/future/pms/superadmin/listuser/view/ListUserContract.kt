package com.future.pms.superadmin.listuser.view

import com.future.pms.core.base.BaseView
import com.future.pms.superadmin.listuser.model.admin.Admin
import com.future.pms.superadmin.listuser.model.customer.CustomerResponse
import com.future.pms.superadmin.listuser.model.superadmin.SuperAdminResponse

interface ListUserContract : BaseView {
  fun loadAllCustomerSuccess(customer: CustomerResponse)
  fun loadAllAdminSuccess(admin: Admin)
  fun loadAllSuperAdminSuccess(superAdmin: SuperAdminResponse)
}