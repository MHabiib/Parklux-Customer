package com.future.pms.splash.view

import android.content.Context
import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Token

interface SplashContract : BaseView {
  fun onSuccess(token: Token)
  fun onLogin()
  fun isAuthenticated(): Context?
  fun refreshToken()
  fun onAuthenticated()
}