package com.future.pms.ui.login

import com.future.pms.ui.base.BaseView

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess()
}