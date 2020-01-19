package com.future.pms.ui.receipt

import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReceiptPresenter : BasePresenter<ReceiptContract>() {

  fun loadData(accessToken: String, idBooking: String) {
    view?.apply {
      val subscribe = api.getBookingReceipt(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
        showProgress(false)
        loadReceiptSuccess(receipt)
      }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }
}