package com.future.pms.superadmin.userdetails.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.customerdetails.Customer
import com.future.pms.superadmin.listuser.model.admin.nonPage.AdminResponse
import com.future.pms.superadmin.userdetails.model.UserDetails

interface UserDetailsContract : BaseView {
  fun loadDataCustomerSuccess(customer: Customer)
  fun loadDataSuperAdminSuccess(userDetails: UserDetails)
  fun loadDataAdminSuccess(adminResponse: AdminResponse?)
  fun deleteSuperAdminSuccess(response: String?)
  fun updateSuperAdminSuccess()
  fun getUpdatedSuperAdminSuccess(userDetails: UserDetails)
  fun updateCustomerSuccess()
  fun getUpdatedCustomerSuccess(customer: Customer)
  fun updateAdminSuccess()
  fun getUpdatedAdminSuccess(adminResponse: AdminResponse?)
  fun showProgress(show: Boolean)
}