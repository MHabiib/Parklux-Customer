package com.future.pms.ui.home

import com.future.pms.model.customerdetail.Body
import com.future.pms.network.ApiServiceInterface
import com.future.pms.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomePresenter @Inject constructor(private val apiServiceInterface: ApiServiceInterface) :
    BasePresenter<HomeContract>() {

  /*fun loadData(accessToken: String) {
    view?.apply {
      val subscribe = api.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
        loadCustomerDetailSuccess(customer)
      }, { error ->
        onFailed(error.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }*/

  fun loadData(accessToken: String) {
      addDisposable(
          apiServiceInterface.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()
          ).subscribe({ customer: Body ->
              view?.loadCustomerDetailSuccess(customer)
          }, { error ->
              view?.onFailed(error.message.toString())
          })
      )
  }

  fun getTextAnnounce(): String {
    return when (Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")).get(
        Calendar.HOUR_OF_DAY)) {
      in 0 .. 11 -> "Good Morning"
      in 12 .. 15 -> "Good Afternoon"
      in 16 .. 20 -> "Good Evening"
      in 21 .. 23 -> "Good Night"
      else -> {
        "Hello,"
      }
    }
  }

  fun onOngoingIconClick() {
    view?.showOngoingFragment()
  }

  fun onHistoryIconClick() {
    view?.showHistoryFragment()
  }
}