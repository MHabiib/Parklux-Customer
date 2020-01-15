package com.future.pms.ui.receipt

import com.future.pms.model.receipt.Receipt
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ReceiptContract

  fun loadData(accessToken: String, idBooking: String) {
    val subscribe = api.getBookingReceipt(idBooking, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
      view.showProgress(false)
      view.loadReceiptSuccess(receipt)
    }, { error ->
      view.showProgress(false)
      view.onFailed(error.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ReceiptContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}