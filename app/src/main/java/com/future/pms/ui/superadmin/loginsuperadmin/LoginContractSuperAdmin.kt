package com.future.pms.ui.superadmin.loginsuperadmin

import com.future.pms.ui.base.BaseView

interface LoginContractSuperAdmin : BaseView {
  fun onSuccess()
  fun onAuthorized()
}