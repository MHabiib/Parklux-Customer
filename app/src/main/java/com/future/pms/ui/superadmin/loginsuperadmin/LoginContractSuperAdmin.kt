package com.future.pms.ui.superadmin.loginsuperadmin

import com.future.pms.di.base.BaseView

interface LoginContractSuperAdmin : BaseView {
  fun onSuccess()
  fun onAuthorized()
}