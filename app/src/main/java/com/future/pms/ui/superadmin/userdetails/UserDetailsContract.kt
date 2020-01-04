package com.future.pms.ui.superadmin.userdetails

import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.user.UserResponse

interface UserDetailsContract {
  fun loadDataCustomerSuccess(customer: Customer)
  fun loadDataSuperAdminSuccess(userResponse: UserResponse)
  fun loadDataAdminSuccess(adminResponse: AdminResponse?)
  fun onFailed(e: String)

}