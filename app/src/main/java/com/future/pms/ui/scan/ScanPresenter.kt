package com.future.pms.ui.scan

import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScanPresenter : BasePresenter<ScanContract>() {

  fun createBooking(idSlot: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.postCreateBooking(idSlot, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        if (null != it) {
          bookingSuccess(it.idBooking)
        }
      }, {
        onFailed(it.message.toString())
      })
      showProgress(false)
      subscriptions.add(subscribe)
    }
  }
}