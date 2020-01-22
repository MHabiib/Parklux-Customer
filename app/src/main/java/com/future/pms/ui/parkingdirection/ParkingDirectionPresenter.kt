package com.future.pms.ui.parkingdirection

import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.bookingdetail.network.BookingDetailApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ParkingDirectionPresenter : BasePresenter<ParkingDirectionContract>() {
  @Inject lateinit var bookingDetailApi: BookingDetailApi

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = bookingDetailApi.getParkingLayout(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          showProgress(false)
          getLayoutSuccess(it)
        }
      }, {
        onFailed(it.message.toString())
      })
      showProgress(false)
      subscriptions.add(subscribe)
    }
  }
}