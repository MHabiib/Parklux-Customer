package com.future.pms.ui.superadmin.listactivity

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListActivityPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ListActivityContract

  fun loadAllBooking(accessToken: String, page: Int) {
    val subscribe = api.loadAllBooking(accessToken, page).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      if (null != it) {
        view.loadAllBookingSuccess(it)
      }
    }, {
      it.message?.let { it1 -> view.onFailed(it1) }
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: ListActivityContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}