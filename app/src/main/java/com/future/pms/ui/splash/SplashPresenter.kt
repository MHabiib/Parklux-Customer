package com.future.pms.ui.splash

import com.future.pms.di.base.BasePresenter
import com.future.pms.util.Authentication

class SplashPresenter : BasePresenter<SplashContract>() {

  fun attach(view: SplashContract) {
    this.view = view
  }

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