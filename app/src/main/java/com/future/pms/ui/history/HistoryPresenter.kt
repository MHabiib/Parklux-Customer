package com.future.pms.ui.history

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor() {
  private val api: ApiServiceInterface = RetrofitClient.create()
  private val subscriptions = CompositeDisposable()
  private lateinit var view: HistoryContract

  fun loadCustomerBooking(accessToken: String, page: Int) {
    val subscribe = api.getCustomerBooking(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadCustomerBookingSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.showErrorMessage(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun subscribe() {}

  fun attach(view: HistoryContract) {
    this.view = view
  }
}