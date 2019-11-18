package com.future.pms.ui.home

import com.future.pms.model.customerdetail.Customer
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: HomeContract

  fun subscribe() {}

  fun unsubscribe() {
    subscriptions.clear()
  }

  fun attach(view: HomeContract) {
    this.view = view
  }

  fun loadData(access_token: String) {
    val subscribe = api.getCustomerDetail(access_token).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ customer: Customer ->
          view.loadCustomerDetailSuccess(customer)
        }, { error ->
          view.showErrorMessage(error.localizedMessage)
          view.unauthorized()
        })
    subscriptions.add(subscribe)
  }

  fun getTextAnnounce(): String {
    return when (Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")).get(
        Calendar.HOUR_OF_DAY)) {
      in 0 .. 11 -> "Good Morning"
      in 12 .. 15 -> "Good Afternoon"
      in 16 .. 20 -> "Good Evening"
      in 21 .. 23 -> "Good Night"
      else -> {
        "Hello"
      }
    }
  }
}