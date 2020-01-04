package com.future.pms.ui.superadmin.listuser

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListUserPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ListUserContract

  fun loadAllCustomer(accessToken: String, page: Int) {
    val subscribe = api.loadAllCustomer(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadAllCustomerSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.onFailed(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllSuperAdmin(accessToken: String, page: Int) {
    val subscribe = api.loadAllSuperAdmin(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadAllSuperAdminSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.onFailed(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun loadAllAdmin(accessToken: String, page: Int) {
    val subscribe = api.loadAllParkingZone(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadAllAdminSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.onFailed(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ListUserContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}