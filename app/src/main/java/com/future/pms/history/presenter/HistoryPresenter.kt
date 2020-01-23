package com.future.pms.history.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.history.network.HistoryApi
import com.future.pms.history.view.HistoryContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor() : BasePresenter<HistoryContract>() {
  @Inject lateinit var historyApi: HistoryApi

  fun loadCustomerBooking(accessToken: String, page: Int) {
    subscriptions.add(
        historyApi.getCustomerBooking(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.loadCustomerBookingSuccess(it)
          }
        }, {
          it.message?.let { throwable -> view?.onFailed(throwable) }
        }))
  }
}