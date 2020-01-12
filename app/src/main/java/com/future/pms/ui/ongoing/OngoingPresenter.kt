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

  fun loadOngoingBooking(accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ ongoing: CustomerBooking ->
      view.showProgress(false)
      view.loadCustomerOngoingSuccess(ongoing)
    }, { error ->
      view.showProgress(false)
      error.message?.let {
        view.loadCustomerOngoingFailed(it)
        view.showErrorMessage(it)
      }
    })
    subscriptions.add(subscribe)
  }

  fun checkoutBooking(accessToken: String) {
    with(view) {
      showProgress(true)
      val subscribe = api.postBookingCheckout(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        checkoutSuccess(it.idBooking)
      }, {
        view.showProgress(false)
        showErrorMessage(it.message.toString())
      })
      subscriptions.add(subscribe)
      refreshHome()
    }
  }

  fun subscribe() {}

  fun attach(view: OngoingContract) {
    this.view = view
  }
}