package com.future.pms.ui.ongoing

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OngoingPresenter : BasePresenter<OngoingContract>() {

  fun loadOngoingBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ ongoing: CustomerBooking ->
        showProgress(false)
        loadCustomerOngoingSuccess(ongoing)
      }, { error ->
        showProgress(false)
        error.message?.let {
          loadCustomerOngoingFailed(it)
          onFailed(it)
        }
      })
      subscriptions.add(subscribe)
    }
  }

  fun checkoutBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = api.postBookingCheckout(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        checkoutSuccess(it.idBooking)
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
      refreshHome()
    }
  }
}