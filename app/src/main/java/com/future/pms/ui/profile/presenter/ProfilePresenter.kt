package com.future.pms.ui.profile.presenter

import com.future.pms.model.customerdetail.Body
import com.future.pms.model.register.CustomerRequest
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.profile.network.ProfileApi
import com.future.pms.ui.profile.view.ProfileContract
import com.future.pms.util.Authentication
import com.future.pms.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileContract>() {
  @Inject lateinit var profileApi: ProfileApi

  fun loadData(accessToken: String) {
    subscriptions.add(
        profileApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
          view?.loadCustomerDetailSuccess(customer)
        }, {
          if (it.message.toString().contains(Constants.UNAUTHORIZED_CODE)) {
            refreshFetcher({ loadData(accessToken) }, { view?.onFailed(it.message.toString()) })
          } else {
            view?.onFailed(it.message.toString())
          }
        }))
  }

  fun update(name: String, email: String, password: String, phoneNumber: String, token: String) {
    view?.apply {
      val customer = CustomerRequest(email, name, password, phoneNumber)
      subscriptions.add(
          profileApi.putUpdateCustomer(token, customer).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            showProgress(false)
            onSuccess()
          }, {
            showProgress(false)
            onFailed(it.message.toString())
          }))
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }
}