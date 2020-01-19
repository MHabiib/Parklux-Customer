package com.future.pms.ui.splash

import com.future.pms.ui.base.BasePresenter
import com.future.pms.util.Authentication

class SplashPresenter : BasePresenter<SplashContract>() {

  fun isAuthenticated() {
    try {
      if (Authentication.isAuthenticated(view?.isAuthenticated())) {
        view?.onSuccess()
      } else {
        refreshFetcher({ view?.onSuccess() }, { view?.onLogin() })
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      view?.onLogin()
    }
  }
}