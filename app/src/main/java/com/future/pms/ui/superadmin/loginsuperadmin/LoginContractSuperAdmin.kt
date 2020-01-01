package com.future.pms.ui.superadmin.loginsuperadmin

import com.future.pms.di.base.BaseMVPView

interface LoginContractSuperAdmin : BaseMVPView {
  fun onSuccess()
  fun onAuthorized()
  fun onFailed(e: String)
  fun onError()
}