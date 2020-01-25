package com.future.pms.superadmin.homesuperadmin.presenter

import com.future.pms.core.base.BasePresenter
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
}