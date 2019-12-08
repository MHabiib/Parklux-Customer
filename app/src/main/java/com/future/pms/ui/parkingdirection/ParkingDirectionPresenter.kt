package com.future.pms.ui.parkingdirection

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ParkingDirectionPresenter @Inject constructor() {
  private val api: ApiServiceInterface = RetrofitClient.create()
  private val subscriptions = CompositeDisposable()
  private lateinit var view: ParkingDirectionContract

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view.showProgress(true)
    val subscribe = api.getParkingLayout(idBooking, accessToken).subscribeOn(
      Schedulers.io()
    ).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.showProgress(false)
        view.getLayoutSuccess(it)
      }
    }, {
      view.getLayoutFailed()
    })
    view.showProgress(false)
    subscriptions.add(subscribe)
  }

  fun attach(view: ParkingDirectionContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}