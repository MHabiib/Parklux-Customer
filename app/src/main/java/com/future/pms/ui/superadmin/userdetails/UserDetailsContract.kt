package com.future.pms.ui.superadmin.userdetails

import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.user.UserResponse

interface UserDetailsContract {
  fun loadDataCustomerSuccess(customer: Customer)
  fun loadDataSuperAdminSuccess(userResponse: UserResponse)
  fun loadDataAdminSuccess(adminResponse: AdminResponse?)
  fun onFailed(e: String)
  fun deleteSuperAdminSuccess(response: String?)
  fun updateSuperAdminSuccess()
  fun getUpdatedSuperAdminSuccess(userResponse: UserResponse)
  fun updateCustomerSuccess()
  fun getUpdatedCustomerSuccess(customer: Customer?)
  fun updateAdminSuccess()
  fun getUpdatedAdminSuccess(adminResponse: AdminResponse?)
  fun showProgress(show: Boolean)
}