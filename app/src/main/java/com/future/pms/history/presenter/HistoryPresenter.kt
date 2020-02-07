package com.future.pms.history.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.history.network.HistoryApi
import com.future.pms.history.view.HistoryContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryPresenter @Inject constructor(private val historyApi: HistoryApi) :
    BasePresenter<HistoryContract>() {

  fun loadCustomerBooking(accessToken: String, page: Int) {
    subscriptions.add(
        historyApi.getCustomerBooking(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          if (null != it) {
            view?.loadCustomerBookingSuccess(it)
          }
        }, {
          view?.onFailed(it.message.toString())
        }))
  }
}