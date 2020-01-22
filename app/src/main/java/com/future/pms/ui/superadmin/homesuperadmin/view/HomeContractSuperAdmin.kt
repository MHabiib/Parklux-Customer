package com.future.pms.ui.superadmin.homesuperadmin.view

import com.future.pms.ui.base.BaseView

interface HomeContractSuperAdmin : BaseView {
  fun onLogout()
  fun createUserSuccess()
  fun updateUserSuccess()
  fun getEmailSuccess(email: String)
}