package com.future.pms.splash.presenter

import android.content.Context
import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.core.network.Authentication
import com.future.pms.splash.network.SplashApi
import com.future.pms.splash.view.SplashContract
import com.future.pms.util.Constants
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {
  @Inject lateinit var splashApi: SplashApi

  fun isAuthenticated() {
    try {
      if (Authentication.isAuthenticated(view?.isAuthenticated())) {
        view?.onSuccess()
      } else {
        getContext()?.let { Authentication.getRefresh(it) }?.let {
          splashApi.refresh(Constants.GRANT_TYPE_REFRESH, it).subscribeOn(
              Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
            getContext()?.let { context ->
              Authentication.save(context, token, Gson().fromJson(
                  context.getSharedPreferences(Constants.AUTHENTICATION,
                      Context.MODE_PRIVATE)?.getString(Constants.TOKEN, null),
                  Token::class.java).role)
            }
            view?.onSuccess()
          }, {
            view?.onLogin()
          })
        }?.let {
          subscriptions.add(it)
        }
      }
    } catch (e: Authentication.WithoutAuthenticatedException) {
      view?.onLogin()
    }
  }
}