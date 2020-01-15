package com.future.pms.ui.login

import com.future.pms.di.base.BaseView

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess()
}