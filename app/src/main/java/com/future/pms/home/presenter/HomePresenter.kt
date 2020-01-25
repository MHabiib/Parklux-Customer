package com.future.pms.home.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.home.network.HomeApi
import com.future.pms.home.view.HomeContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor() : BasePresenter<HomeContract>() {
  @Inject lateinit var mHomeApi: HomeApi

  fun loadData(accessToken: String) {
    view?.apply {
      subscriptions.add(
          mHomeApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
            loadCustomerDetailSuccess(customer)
          }, { error ->
            onFailed(error.message.toString())
          }))
    }
  }

  fun onOngoingIconClick() {
    view?.showOngoingFragment()
  }

  fun onHistoryIconClick() {
    view?.showHistoryFragment()
  }
}