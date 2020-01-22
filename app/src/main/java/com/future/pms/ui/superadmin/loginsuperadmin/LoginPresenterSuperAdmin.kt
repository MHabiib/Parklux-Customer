package com.future.pms.ui.superadmin.loginsuperadmin

import com.future.pms.di.module.APICreator
import com.future.pms.model.oauth.Token
import com.future.pms.network.ApiServiceInterface
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.login.network.LoginApi
import com.future.pms.util.Authentication
import com.future.pms.util.Constants.Companion.GRANT_TYPE
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenterSuperAdmin : BasePresenter<LoginContractSuperAdmin>() {
  @Inject lateinit var loginApi: LoginApi

  fun login(username: String, password: String) {
    val authFetcher = APICreator(ApiServiceInterface::class.java).generate()
    val subscribe = loginApi.auth(username, password, GRANT_TYPE).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      getContext()?.let { Authentication.save(it, token, ROLE_SUPER_ADMIN) }
      view?.let { view?.onSuccess() }
    }, { view?.onFailed(it.message.toString()) })
    subscriptions.add(subscribe)
  }

  fun isSuperAdmin(accessToken: String) {
    val subscribe = api.isSuperAdmin(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.onAuthorized()
      }
    }, {
      getContext()?.let(Authentication::delete)
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }
}