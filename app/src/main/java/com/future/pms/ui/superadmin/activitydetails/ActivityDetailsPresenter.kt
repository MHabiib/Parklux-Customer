package com.future.pms.ui.superadmin.activitydetails

import com.future.pms.model.receipt.Receipt
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActivityDetailsPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ActivityDetailsContract

  fun bookingReceiptSA(idBooking: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.bookingReceiptSA(idBooking, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
      view.showProgress(false)
      view.bookingReceiptSASuccess(receipt)
    }, { error ->
      view.showProgress(false)
      view.showErrorMessage(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun checkoutBookingSA(idBooking: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.checkoutBookingSA(idBooking, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view.showProgress(false)
      view.checkoutBookingSASuccess(it)
    }, { error ->
      view.showProgress(false)
      view.showErrorMessage(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ActivityDetailsContract) {
    this.view = view
  }
}