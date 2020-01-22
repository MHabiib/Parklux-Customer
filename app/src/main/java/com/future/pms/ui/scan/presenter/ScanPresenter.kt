package com.future.pms.ui.scan.presenter

import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.scan.network.ScanApi
import com.future.pms.ui.scan.view.ScanContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScanPresenter @Inject constructor() : BasePresenter<ScanContract>() {
  @Inject lateinit var scanApi: ScanApi

  fun createBooking(idSlot: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          scanApi.postCreateBooking(idSlot, accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            if (null != it) {
              bookingSuccess(it.idBooking)
            }
          }, {
            onFailed(it.message.toString())
          }))
      showProgress(false)
    }
  }
}