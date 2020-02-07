package com.future.pms.parkingdirection.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.parkingdirection.network.ParkingDirectionApi
import com.future.pms.parkingdirection.view.ParkingDirectionContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ParkingDirectionPresenter @Inject constructor(
    private val parkingDirectionApi: ParkingDirectionApi) :
    BasePresenter<ParkingDirectionContract>() {

  fun getParkingLayout(idBooking: String, accessToken: String) {
    view?.apply {
      subscriptions.add(parkingDirectionApi.getParkingLayout(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        getLayoutSuccess(it)
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      }))
    }
  }
}