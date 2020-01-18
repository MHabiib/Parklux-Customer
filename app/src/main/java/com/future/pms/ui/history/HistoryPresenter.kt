package com.future.pms.ui.history

import com.future.pms.di.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryPresenter : BasePresenter<HistoryContract>() {
  fun loadCustomerBooking(accessToken: String, page: Int) {
    val subscribe = api.getCustomerBooking(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadCustomerBookingSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: HistoryContract) {
    this.view = view
  }
}