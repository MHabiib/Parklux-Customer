package com.future.pms.maps.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.maps.network.MapsApi
import com.future.pms.maps.view.MapsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapsPresenter @Inject constructor(private val mapsApi: MapsApi) :
    BasePresenter<MapsContract>() {

  fun loadLocation(accessToken: String) {
    subscriptions.add(mapsApi.getLatLng(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.loadLocationSuccess(it)
    }, {
      view?.onFailed(it.message.toString())
    }))
  }
}