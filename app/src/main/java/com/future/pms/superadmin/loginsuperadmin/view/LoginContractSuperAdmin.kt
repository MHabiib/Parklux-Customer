package com.future.pms.superadmin.loginsuperadmin.view

import com.future.pms.core.base.BaseView

interface LoginContractSuperAdmin : BaseView {
  fun onSuccess()
  fun onAuthorized()
}