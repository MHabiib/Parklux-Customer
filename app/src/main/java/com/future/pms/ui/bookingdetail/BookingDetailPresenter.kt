package com.future.pms.ui.bookingdetail

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BookingDetailPresenter : BasePresenter<BookingDetailContract>() {

  fun attach(view: BookingDetailContract) {
    this.view = view
  }

  fun loadBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ booking: CustomerBooking ->
        showProgress(false)
        loadBookingSuccess(booking)
      }, { error ->
        showProgress(false)
        showErrorMessage(error.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getParkingLayout(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          showProgress(false)
          getLayoutSuccess(it)
        }
      }, {
        it.message?.let { throwable -> this.showErrorMessage(throwable) }
      })
      showProgress(false)
      subscriptions.add(subscribe)
    }
  }
}