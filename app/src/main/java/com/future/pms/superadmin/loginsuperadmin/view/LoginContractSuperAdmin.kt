package com.future.pms.superadmin.loginsuperadmin.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Token

interface LoginContractSuperAdmin : BaseView {
  fun onAuthorized()
  fun onSuccess(token: Token)
}