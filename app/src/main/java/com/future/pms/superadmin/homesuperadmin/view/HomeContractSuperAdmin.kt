package com.future.pms.superadmin.homesuperadmin.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Token

interface HomeContractSuperAdmin : BaseView {
  fun onLogout()
  fun createUserSuccess()
  fun updateUserSuccess()
  fun getEmailSuccess(email: String)
  fun onSuccess(token: Token)
}