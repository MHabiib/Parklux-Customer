package com.future.pms.ui.splash

import android.content.Context

interface SplashContract {
  fun onSuccess()
  fun onLogin()
  fun onError(e: Throwable)
  fun isAuthenticated(): Context?
  fun refreshFetcher()
}