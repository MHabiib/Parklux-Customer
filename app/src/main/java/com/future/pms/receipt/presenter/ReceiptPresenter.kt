package com.future.pms.receipt.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Receipt
import com.future.pms.receipt.network.ReceiptApi
import com.future.pms.receipt.view.ReceiptContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReceiptPresenter @Inject constructor(private val receiptApi: ReceiptApi) :
    BasePresenter<ReceiptContract>() {

  fun loadData(accessToken: String, id: String) {
    view?.apply {
      subscriptions.add(receiptApi.getBookingReceipt(id, accessToken).subscribeOn(
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