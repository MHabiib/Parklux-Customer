package com.future.pms.login.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.login.network.LoginApi
import com.future.pms.login.view.LoginContract
import com.future.pms.util.Constants.Companion.GRANT_TYPE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor(private val loginApi: LoginApi) :
    BasePresenter<LoginContract>() {

  fun login(username: String, password: String) {
    subscriptions.add(
        loginApi.auth(username, password, GRANT_TYPE).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({ token: Token ->
          view?.onSuccess(token)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }

  fun loadData(accessToken: String) {
    subscriptions.add(
        loginApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.onAuthorized()
        }, {
            view?.onFailed(it.message.toString())
        }))
  }
}