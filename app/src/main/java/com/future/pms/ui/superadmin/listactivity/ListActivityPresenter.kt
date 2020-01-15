package com.future.pms.ui.superadmin.listactivity

import com.future.pms.di.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListActivityPresenter @Inject constructor() : BasePresenter<ListActivityContract>() {

  fun loadAllBooking(accessToken: String, page: Int) {
    val subscribe = api.loadAllBooking(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.loadAllBookingSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun findBookingById(idBooking: String, accessToken: String) {
    val subscribe = api.findBookingById(idBooking, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view?.findBookingByIdSuccess(it)
      }
    }, {
      it.message?.let { throwable -> view?.onFailed(throwable) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ListActivityContract) {
    this.view = view
  }
}