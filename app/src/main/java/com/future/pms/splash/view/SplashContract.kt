package com.future.pms.splash.view

import android.content.Context
import com.future.pms.core.base.BaseView

interface SplashContract : BaseView {
  fun onSuccess()
  fun onLogin()
  fun isAuthenticated(): Context?
}