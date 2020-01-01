package com.future.pms.ui.superadmin.listparkingzone

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListParkingZonePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ListParkingZoneContract

  fun loadAllParkingZone(accessToken: String, page: Int) {
    val subscribe = api.loadAllParkingZone(accessToken, page).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadAllParkingZoneSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.onFailed(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ListParkingZoneContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}