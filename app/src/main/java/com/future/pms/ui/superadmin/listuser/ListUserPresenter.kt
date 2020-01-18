package com.future.pms.ui.superadmin.listuser

import com.future.pms.di.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListUserPresenter @Inject constructor() : BasePresenter<ListUserContract>() {

  fun loadAllCustomer(accessToken: String, page: Int, name: String) {
    val subscribe = api.loadAllCustomer(accessToken, page, name).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllCustomerSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllSuperAdmin(accessToken: String, page: Int) {
    val subscribe = api.loadAllSuperAdmin(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllSuperAdminSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllAdmin(accessToken: String, page: Int, name: String) {
    val subscribe = api.loadAllParkingZone(accessToken, page, name).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllAdminSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ListUserContract) {
    this.view = view
  }
}