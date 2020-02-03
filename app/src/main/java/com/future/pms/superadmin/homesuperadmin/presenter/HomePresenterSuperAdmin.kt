package com.future.pms.superadmin.homesuperadmin.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Token
import com.future.pms.superadmin.homesuperadmin.network.HomeApiSuperAdmin
import com.future.pms.superadmin.homesuperadmin.view.HomeContractSuperAdmin
import com.future.pms.superadmin.userdetails.model.User
import com.future.pms.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenterSuperAdmin @Inject constructor(
    private val homeApiSuperAdmin: HomeApiSuperAdmin) : BasePresenter<HomeContractSuperAdmin>() {

  fun createUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    subscriptions.add(homeApiSuperAdmin.createUser(accessToken, user).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.createUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    }))
  }

  fun updateUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    subscriptions.add(homeApiSuperAdmin.updateUser(accessToken, user).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.updateUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    }))
  }

  fun getEmail(accessToken: String) {
    subscriptions.add(
        homeApiSuperAdmin.getEmail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.getEmailSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
        }))
  }

  fun refreshToken(refreshToken: String) {
    subscriptions.add(
        homeApiSuperAdmin.refresh(Constants.GRANT_TYPE_REFRESH, refreshToken).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ token: Token ->
          view?.onSuccess(token)
        }, {
          view?.onLogout()
        }))
  }
}