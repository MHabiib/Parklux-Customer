package com.future.pms.superadmin.homesuperadmin.view

import com.future.pms.core.base.BaseView

interface HomeContractSuperAdmin : BaseView {
  fun onLogout()
  fun createUserSuccess()
  fun updateUserSuccess()
  fun getEmailSuccess(email: String)
}