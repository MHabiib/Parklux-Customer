package com.future.pms.ui.parkingdirection.presenter

import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.parkingdirection.network.ParkingDirectionApi
import com.future.pms.ui.parkingdirection.view.ParkingDirectionContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ParkingDirectionPresenter @Inject constructor() : BasePresenter<ParkingDirectionContract>() {
  @Inject lateinit var parkingDirectionApi: ParkingDirectionApi

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = parkingDirectionApi.getParkingLayout(idBooking, accessToken).subscribeOn(
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