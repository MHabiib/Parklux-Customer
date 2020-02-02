package com.future.pms.superadmin.loginsuperadmin.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.superadmin.loginsuperadmin.network.LoginSuperAdminApi
import com.future.pms.superadmin.loginsuperadmin.view.LoginContractSuperAdmin
import com.future.pms.util.Constants.Companion.GRANT_TYPE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenterSuperAdmin @Inject constructor() : BasePresenter<LoginContractSuperAdmin>() {
  @Inject lateinit var loginSuperAdminApi: LoginSuperAdminApi

  fun login(username: String, password: String) {
    subscriptions.add(loginSuperAdminApi.auth(username, password, GRANT_TYPE).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      view?.let { view?.onSuccess(token) }
    }, { view?.onFailed(it.message.toString()) }))
  }

  fun isSuperAdmin(accessToken: String) {
    val subscribe = loginSuperAdminApi.isSuperAdmin(accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.onAuthorized()
      }
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }
}