package com.future.pms.ui.superadmin.homesuperadmin

import com.future.pms.di.base.BaseMVPView

interface HomeContractSuperAdmin : BaseMVPView {
  fun onFailed(e: String)
  fun onLogout()
  fun createUserSuccess()
  fun updateUserSuccess()
  fun getEmailSuccess(email: String)
}