package com.future.pms.ui.receipt.presenter

import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.receipt.network.ReceiptApi
import com.future.pms.ui.receipt.view.ReceiptContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptPresenter @Inject constructor() : BasePresenter<ReceiptContract>() {
  @Inject lateinit var receiptApi: ReceiptApi

  fun loadData(accessToken: String, idBooking: String) {
    view?.apply {
      subscriptions.add(receiptApi.getBookingReceipt(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
        showProgress(false)
        loadReceiptSuccess(receipt)
      }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      }))
    }
  }
}