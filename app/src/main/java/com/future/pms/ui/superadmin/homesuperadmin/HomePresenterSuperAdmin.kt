package com.future.pms.ui.superadmin.homesuperadmin

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.user.User
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomePresenterSuperAdmin : BasePresenter<HomeContractSuperAdmin>() {

  fun createUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = api.createUser(accessToken, user).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.createUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = api.updateUser(accessToken, user).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.updateUserSuccess()
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getEmail(accessToken: String) {
    val subscribe = api.getEmail(accessToken).subscribeOn(Schedulers.io()).observeOn(
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

  fun attach(view: HomeContractSuperAdmin) {
    this.view = view
  }
}