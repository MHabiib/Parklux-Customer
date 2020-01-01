package com.future.pms.ui.login

import com.future.pms.di.base.BaseMVPView

interface LoginContract : BaseMVPView {
  fun onSuccess()
  fun onAuthorized()
  fun onFailed(e: String)
  fun onError()
}