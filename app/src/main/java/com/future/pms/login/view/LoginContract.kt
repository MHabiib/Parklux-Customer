package com.future.pms.login.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Token

interface LoginContract : BaseView {
  fun onAuthorized()
  fun onSuccess(token: Token)
}