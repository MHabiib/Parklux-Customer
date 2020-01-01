package com.future.pms.ui.superadmin.listcustomer

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListCustomerPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ListCustomerContract

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

  fun attach(view: ListCustomerContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}