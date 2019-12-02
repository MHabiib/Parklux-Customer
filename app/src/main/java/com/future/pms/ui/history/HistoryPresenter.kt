package com.future.pms.ui.history

import com.future.pms.model.customerbooking.CustomerBooking
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

  fun loadCustomerBooking(accessToken: String) {
    with(view) {
      val subscribe = api.getCustomerBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ list: List<CustomerBooking> ->
        showProgress(false)
        loadCustomerBookingSuccess(list)
      }, { error ->
        showProgress(false)
        loadCustomerBookingError()
        showErrorMessage(error.localizedMessage)
      })
      subscriptions.add(subscribe)
    }
  }

  fun subscribe() {}

  fun attach(view: HistoryContract) {
    this.view = view
  }
}