package com.future.pms.ui.superadmin.userdetails

import com.future.pms.di.base.BaseView
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.user.UserDetails

interface UserDetailsContract : BaseView {
  fun loadDataCustomerSuccess(customer: Customer)
  fun loadDataSuperAdminSuccess(userDetails: UserDetails)
  fun loadDataAdminSuccess(adminResponse: AdminResponse?)
  fun deleteSuperAdminSuccess(response: String?)
  fun updateSuperAdminSuccess()
  fun getUpdatedSuperAdminSuccess(userDetails: UserDetails)
  fun updateCustomerSuccess()
  fun getUpdatedCustomerSuccess(customer: Customer?)
  fun updateAdminSuccess()
  fun getUpdatedAdminSuccess(adminResponse: AdminResponse?)
  fun showProgress(show: Boolean)
}