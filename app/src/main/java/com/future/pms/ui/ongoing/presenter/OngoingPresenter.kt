package com.future.pms.ui.ongoing.presenter

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.ongoing.OngoingContract
import com.future.pms.ui.ongoing.network.OngoingApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OngoingPresenter @Inject constructor() : BasePresenter<OngoingContract>() {
  @Inject lateinit var ongoingApi: OngoingApi

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
            error.message?.let {
              loadCustomerOngoingFailed(it)
              onFailed(it)
            }
          }))
    }
  }

  fun checkoutBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          ongoingApi.postBookingCheckout(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            showProgress(false)
            checkoutSuccess(it.idBooking)
          }, {
            showProgress(false)
            onFailed(it.message.toString())
          }))
      refreshHome()
    }
  }
}