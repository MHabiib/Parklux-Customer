package com.future.pms.ui.splash

import android.content.Context
import com.future.pms.di.base.BaseView

interface SplashContract : BaseView {
  fun onSuccess()
  fun onLogin()
  fun isAuthenticated(): Context?
  fun refreshFetcher()
}