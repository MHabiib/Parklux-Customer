package com.future.pms.ui.ongoing

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OngoingPresenter @Inject constructor() {
  private val api: ApiServiceInterface = RetrofitClient.create()
  private val subscriptions = CompositeDisposable()
  private lateinit var view: OngoingContract

  fun loadOngoingBooking(access_token: String) {
    view.showProgress(true)
    val subscribe = api.getOngoingBooking(access_token).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ ongoing: CustomerBooking ->
      view.showProgress(false)
      view.loadCustomerOngoingSuccess(ongoing)
    }, { error ->
      view.showProgress(false)
      view.loadCustomerOngoingFailed()
      view.showErrorMessage(error.localizedMessage)
    })
    subscriptions.add(subscribe)
  }

  fun checkoutBooking(access_token: String) {
    view.showProgress(true)
    val subscribe = api.postBookingCheckout(access_token).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.showProgress(false)
      view.checkoutSuccess()
    }, {
      view.showErrorMessage(it.message.toString())
    })
    subscriptions.add(subscribe)
    view.refreshHome()
  }

  fun subscribe() {}

  fun attach(view: OngoingContract) {
    this.view = view
  }
}