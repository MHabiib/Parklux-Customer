package com.future.pms.ui.scan

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ScanPresenter @Inject constructor() {
  private val api: ApiServiceInterface = RetrofitClient.create()
  private val subscriptions = CompositeDisposable()
  private lateinit var view: ScanContract

  fun createBooking(idSlot: String, access_token: String) {
    view.showProgress(true)
    val subscribe = api.postCreateBooking(idSlot, access_token).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.bookingSuccess(it.idBooking)
      }
    }, {
      view.bookingFailed()
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun attach(view: ScanContract) {
    this.view = view
  }
}