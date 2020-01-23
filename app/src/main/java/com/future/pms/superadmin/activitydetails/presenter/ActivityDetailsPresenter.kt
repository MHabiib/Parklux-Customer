package com.future.pms.superadmin.activitydetails.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Receipt
import com.future.pms.superadmin.activitydetails.network.ActivityDetailsApi
import com.future.pms.superadmin.activitydetails.view.ActivityDetailsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ActivityDetailsPresenter @Inject constructor() : BasePresenter<ActivityDetailsContract>() {
  @Inject lateinit var activityDetailsApi: ActivityDetailsApi

  fun bookingReceiptSA(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = activityDetailsApi.bookingReceiptSA(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ receipt: Receipt ->
        showProgress(false)
        bookingReceiptSASuccess(receipt)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun checkoutBookingSA(idBooking: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = activityDetailsApi.checkoutBookingSA(idBooking, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        checkoutBookingSASuccess(it)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }
}