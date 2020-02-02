package com.future.pms.splash.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.splash.network.SplashApi
import com.future.pms.splash.view.SplashContract
import com.future.pms.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashContract>() {
  @Inject lateinit var splashApi: SplashApi

  fun refreshToken(refreshToken: String) {
    subscriptions.add(splashApi.refresh(Constants.GRANT_TYPE_REFRESH, refreshToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      view?.onSuccess(token)
    }, {
      view?.onLogin()
    }))
  }
}