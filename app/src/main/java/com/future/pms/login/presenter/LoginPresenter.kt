package com.future.pms.login.presenter

import android.content.Context
import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.core.network.Authentication
import com.future.pms.login.network.LoginApi
import com.future.pms.login.view.LoginContract
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.GRANT_TYPE
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.future.pms.util.Constants.Companion.UNAUTHORIZED_CODE
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract>() {
  @Inject lateinit var loginApi: LoginApi

  fun login(username: String, password: String) {
    subscriptions.add(
        loginApi.auth(username, password, GRANT_TYPE).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({ token: Token ->
          getContext()?.let { Authentication.save(it, token, ROLE_CUSTOMER) }
          view?.onSuccess()
        }, { it.message?.let { throwable -> view?.onFailed(throwable) } }))
  }

  fun loadData(accessToken: String) {
    subscriptions.add(
        loginApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.onAuthorized()
        }, {
          if (it.message.toString().contains(UNAUTHORIZED_CODE)) {
            getContext()?.let { Authentication.getRefresh(it) }?.let {
              loginApi.refresh(Constants.GRANT_TYPE_REFRESH, it).subscribeOn(
                  Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                  { token: Token ->
                    getContext()?.let { context ->
                      Authentication.save(context, token, Gson().fromJson(
                          context.getSharedPreferences(Constants.AUTHENTICATION,
                              Context.MODE_PRIVATE)?.getString(Constants.TOKEN, null),
                          Token::class.java).role)
                    }
                    loadData(accessToken)
                  }, { throwable ->
                view?.onFailed(throwable.message.toString())
              })
            }?.let {
              subscriptions.add(it)
            }
          } else {
            getContext()?.let { context -> Authentication.delete(context) }
            view?.onFailed(it.message.toString())
          }
        }))
  }
}