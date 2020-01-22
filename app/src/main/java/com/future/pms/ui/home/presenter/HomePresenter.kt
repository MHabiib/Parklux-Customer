package com.future.pms.ui.home.presenter

import com.future.pms.model.customerdetail.Body
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.home.network.IHomeApi
import com.future.pms.ui.home.view.HomeContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomePresenter @Inject constructor() : BasePresenter<HomeContract>() {
  @Inject lateinit var mIHomeApi: IHomeApi

  fun loadData(accessToken: String) {
    view?.apply {
      subscriptions.add(
          mIHomeApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
        loadCustomerDetailSuccess(customer)
      }, { error ->
        onFailed(error.message.toString())
          }))
    }
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