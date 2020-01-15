package com.future.pms.ui.login

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.oauth.Token
import com.future.pms.network.APICreator
import com.future.pms.network.AuthAPI
import com.future.pms.network.NetworkConstant.GRANT_TYPE
import com.future.pms.util.Authentication
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.UNAUTHORIZED_CODE
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor() : BasePresenter<LoginContract>() {

  fun attach(view: LoginContract) {
    this.view = view
  }

  fun login(username: String, password: String) {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe = authFetcher.auth(username, password, GRANT_TYPE).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
      getContext()?.let { Authentication.save(it, token, ROLE_ADMIN) }
      view?.onSuccess()
    }, { it.message?.let { throwable -> view?.onFailed(throwable) } })
    subscriptions.add(subscribe)
  }

  fun loadData(accessToken: String) {
    val subscribe = api.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.onAuthorized()
    }, {
      if (it.message.toString().contains(UNAUTHORIZED_CODE)) {
        refreshFetcher({ loadData(accessToken) }, { view?.onFailed(it.message.toString()) })
      }
      getContext()?.let { context -> Authentication.delete(context) }
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  private fun refreshFetcher(functionOnSuccess: () -> Unit, functionOnFailed: () -> Unit) {
    val authFetcher = APICreator(AuthAPI::class.java).generate()
    val subscribe = getContext()?.let { Authentication.getRefresh(it) }?.let {
      authFetcher.refresh(GRANT_TYPE, it).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ token: Token ->
        getContext()?.let { context -> Authentication.save(context, token, token.role) }
        functionOnSuccess()
      }, { functionOnFailed() })
    }
    subscribe?.let { subscriptions.add(it) }
  }
}