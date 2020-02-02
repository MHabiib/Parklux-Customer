package com.future.pms.superadmin.listactivity.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.superadmin.listactivity.network.ListActivityApi
import com.future.pms.superadmin.listactivity.view.ListActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListActivityPresenter @Inject constructor() : BasePresenter<ListActivityContract>() {
  @Inject lateinit var listActivityApi: ListActivityApi

  fun loadAllBooking(accessToken: String, page: Int, filter: String) {
    subscriptions.add(listActivityApi.loadAllBooking(accessToken, page, filter).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.loadAllBookingSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
    }))
  }

  fun findBookingById(idBooking: String, accessToken: String) {
    subscriptions.add(listActivityApi.findBookingById(idBooking, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.findBookingByIdSuccess(it)
      }
    }, {
      view?.onFailed(it.message.toString())
    }))
  }
}