package com.future.pms.bookingdetail.presenter

import com.future.pms.bookingdetail.network.BookingDetailApi
import com.future.pms.bookingdetail.view.BookingDetailContract
import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.CustomerBooking
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookingDetailPresenter @Inject constructor() : BasePresenter<BookingDetailContract>() {
  @Inject lateinit var bookingDetailApi: BookingDetailApi

  fun loadBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          bookingDetailApi.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ booking: CustomerBooking ->
            showProgress(false)
            loadBookingSuccess(booking)
          }, { error ->
            showProgress(false)
            showErrorMessage(error.message.toString())
          }))
    }
  }

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(bookingDetailApi.getParkingLayout(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          showProgress(false)
          getLayoutSuccess(it)
        }
      }, {
        it.message?.let { throwable -> this.showErrorMessage(throwable) }
      }))
      showProgress(false)
    }
  }
}