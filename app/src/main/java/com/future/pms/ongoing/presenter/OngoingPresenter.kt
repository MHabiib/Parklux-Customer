package com.future.pms.ongoing.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.CustomerBooking
import com.future.pms.ongoing.database.CheckoutStepOneUrl
import com.future.pms.ongoing.database.CheckoutStepOneUrlDatabase
import com.future.pms.ongoing.network.OngoingApi
import com.future.pms.ongoing.view.OngoingContract
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OngoingPresenter @Inject constructor(private val ongoingApi: OngoingApi) :
    BasePresenter<OngoingContract>() {
  private var checkoutStepOneUrlDatabase: CheckoutStepOneUrlDatabase? = null
  val students: ArrayList<CheckoutStepOneUrl> = ArrayList()

  fun loadOngoingBooking(accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(
          ongoingApi.getOngoingBooking(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ ongoing: CustomerBooking ->
            showProgress(false)
            loadCustomerOngoingSuccess(ongoing)
            getAllData()
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
      subscriptions.add(ongoingApi.postBookingCheckout(fcmToken, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        checkoutSuccess(it.string())
        insertToDb(CheckoutStepOneUrl(it.string()))
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      }))
      refreshHome()
    }
  }

  fun insertToDb(checkoutStepOneUrl: CheckoutStepOneUrl) {
    subscriptions.add(Observable.fromCallable {
      checkoutStepOneUrlDatabase?.checkoutStepOneDao()?.insert(checkoutStepOneUrl)
    }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe())
  }
  //todo

  fun getAllData() {
    subscriptions.add(
        Observable.fromCallable { checkoutStepOneUrlDatabase?.checkoutStepOneDao()?.getAll() }.subscribeOn(
            Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe {
              students.clear()
              students.addAll(it)
            })
  }

}