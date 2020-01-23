package com.future.pms.superadmin.homesuperadmin.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.network.Authentication
import com.future.pms.superadmin.homesuperadmin.network.HomeApiSuperAdmin
import com.future.pms.superadmin.homesuperadmin.view.HomeContractSuperAdmin
import com.future.pms.superadmin.userdetails.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenterSuperAdmin @Inject constructor() : BasePresenter<HomeContractSuperAdmin>() {
  @Inject lateinit var homeApiSuperAdmin: HomeApiSuperAdmin

  fun createUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = homeApiSuperAdmin.createUser(accessToken, user).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.createUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = homeApiSuperAdmin.updateUser(accessToken, user).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.updateUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getEmail(accessToken: String) {
    val subscribe = homeApiSuperAdmin.getEmail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.getEmailSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }
}