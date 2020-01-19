package com.future.pms.ui.parkingdirection

import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ParkingDirectionPresenter : BasePresenter<ParkingDirectionContract>() {

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
        onFailed(it.message.toString())
      })
      showProgress(false)
      subscriptions.add(subscribe)
    }
  }
}