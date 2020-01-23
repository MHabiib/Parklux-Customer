package com.future.pms.login.view

import com.future.pms.core.base.BaseView

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess()
}