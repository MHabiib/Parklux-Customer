package com.future.pms.ui.login

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.oauth.Token
import com.future.pms.network.APICreator
import com.future.pms.network.AuthAPI
import com.future.pms.network.NetworkConstant.GRANT_TYPE
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract>() {
  private val subscriptions = CompositeDisposable()

  fun subscribe() {}

  fun attach(view: LoginContract) {
    this.view = view
  }

  fun login(username: String, password: String) {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe = authFetcher.auth(username, password, GRANT_TYPE).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      getContext()?.let { Authentication.save(it, token) }
      view?.let { view -> call(view, view::onSuccess) }
    }, { view?.onError() })
    subscriptions.add(subscribe)
  }
}