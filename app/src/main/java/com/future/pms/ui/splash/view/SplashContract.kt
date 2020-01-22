package com.future.pms.ui.splash.view

import android.content.Context
import com.future.pms.ui.base.BaseView

interface SplashContract : BaseView {
  fun onSuccess()
  fun onLogin()
  fun isAuthenticated(): Context?
}