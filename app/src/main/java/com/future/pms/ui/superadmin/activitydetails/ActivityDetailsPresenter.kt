package com.future.pms.ui.superadmin.activitydetails

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.receipt.Receipt
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ActivityDetailsPresenter : BasePresenter<ActivityDetailsContract>() {

  fun bookingReceiptSA(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.bookingReceiptSA(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
        showProgress(false)
        bookingReceiptSASuccess(receipt)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun checkoutBookingSA(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.checkoutBookingSA(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        checkoutBookingSASuccess(it)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun attach(view: ActivityDetailsContract) {
    this.view = view
  }
}