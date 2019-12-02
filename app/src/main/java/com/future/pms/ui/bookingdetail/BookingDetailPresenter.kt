package com.future.pms.ui.bookingdetail

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookingDetailPresenter @Inject constructor() {
  private val api: ApiServiceInterface = RetrofitClient.create()
  private val subscriptions = CompositeDisposable()
  private lateinit var view: BookingDetailContract

  fun loadBooking(accessToken: String) {
    with(view) {
      showProgress(true)
      val subscribe = api.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ booking: CustomerBooking ->
        showProgress(false)
        loadBookingSuccess(booking)
      }, { error ->
        showProgress(false)
        showErrorMessage(error.localizedMessage)
      })
      subscriptions.add(subscribe)
    }
  }

  fun subscribe() {}

  fun attach(view: BookingDetailContract) {
    this.view = view
  }
}