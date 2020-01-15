package com.future.pms.ui.splash

import android.content.Context
import com.future.pms.di.base.BasePresenter
import com.future.pms.model.oauth.Token
import com.future.pms.network.APICreator
import com.future.pms.network.AuthAPI
import com.future.pms.network.NetworkConstant
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {

  fun attach(view: SplashContract) {
    this.view = view
  }

  fun isAuthenticated() {
    try {
      if (Authentication.isAuthenticated(view?.isAuthenticated())) {
        view?.onSuccess()
      } else {
        view?.refreshFetcher()
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      view?.onLogin()
    }
  }

  fun refreshFetcher(context: Context) {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe = authFetcher.refresh(NetworkConstant.GRANT_TYPE,
        Authentication.getRefresh(context)).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      Authentication.save(context, token, token.role)
      view?.onSuccess()
    }, { view?.onLogin() })
    subscriptions.add(subscribe)
  }
}