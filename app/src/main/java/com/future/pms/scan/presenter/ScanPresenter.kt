package com.future.pms.scan.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.scan.network.ScanApi
import com.future.pms.scan.view.ScanContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScanPresenter @Inject constructor(private val scanApi: ScanApi) :
    BasePresenter<ScanContract>() {

  fun createBooking(idSlot: String, fcm: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(scanApi.postCreateBooking(idSlot, fcm, accessToken).subscribeOn(
          Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            bookingSuccess(it.idBooking)
          }, {
            onFailed(it.message.toString())
          }))
      showProgress(false)
    }
  }
}