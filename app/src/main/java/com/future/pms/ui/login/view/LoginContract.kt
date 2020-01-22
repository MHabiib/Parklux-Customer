package com.future.pms.ui.login.view

import com.future.pms.ui.base.BaseView

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess()
}