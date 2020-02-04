package com.future.pms.ongoing.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.CustomerBooking
import com.future.pms.ongoing.network.OngoingApi
import com.future.pms.ongoing.view.OngoingContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OngoingPresenter @Inject constructor(private val ongoingApi: OngoingApi) :
    BasePresenter<OngoingContract>() {

  fun loadOngoingBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          ongoingApi.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ ongoing: CustomerBooking ->
            showProgress(false)
            loadCustomerOngoingSuccess(ongoing)
          }, { error ->
            showProgress(false)
            loadCustomerOngoingFailed(error.message.toString())
            onFailed(error.message.toString())
          }))
    }
  }

    fun checkoutBooking(accessToken: String, fcmToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          ongoingApi.postBookingCheckout(
              fcmToken,
              accessToken
          ).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            showProgress(false)
              checkoutSuccess(it.string())
          }, {
            showProgress(false)
            onFailed(it.message.toString())
          }))
      refreshHome()
    }
  }
}