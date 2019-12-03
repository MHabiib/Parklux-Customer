package com.future.pms.ui.splash

import com.future.pms.util.Authentication
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private lateinit var view: SplashContract

  fun subscribe() {}

  fun unsubscribe() {
    subscriptions.clear()
  }

  fun attach(view: SplashContract) {
    this.view = view
  }

  fun isAuthenticated() {
    try {
      if (Authentication.isAuthenticated(view.isAuthenticated())) {
        view.onSuccess()
      } else {
        view.refreshFetcher()
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      view.onLogin()
    }
  }
}