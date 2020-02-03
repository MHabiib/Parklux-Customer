package com.future.pms.superadmin.listuser.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.superadmin.listuser.network.ListUserApi
import com.future.pms.superadmin.listuser.view.ListUserContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListUserPresenter @Inject constructor(private val listUserApi: ListUserApi) :
    BasePresenter<ListUserContract>() {

  fun loadAllCustomer(accessToken: String, page: Int, name: String) {
    val subscribe = listUserApi.loadAllCustomer(accessToken, page, name).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllCustomerSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllSuperAdmin(accessToken: String, page: Int) {
    val subscribe = listUserApi.loadAllSuperAdmin(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllSuperAdminSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllAdmin(accessToken: String, page: Int, name: String) {
    val subscribe = listUserApi.loadAllParkingZone(accessToken, page, name).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllAdminSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }
}