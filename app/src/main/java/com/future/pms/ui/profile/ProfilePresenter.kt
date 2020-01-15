package com.future.pms.ui.profile

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.register.CustomerRequest
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileContract>() {

  fun loadData(accessToken: String) {
    val subscribe = api.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ customer: Customer ->
      view?.loadCustomerDetailSuccess(customer)
    }, { error ->
      view?.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun update(name: String, email: String, password: String, phoneNumber: String, token: String) {
    view?.apply {
      val customer = CustomerRequest(email, name, password, phoneNumber)
      val subscribe = api.putUpdateCustomer(token, customer).subscribeOn(Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        onSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }

  fun attach(view: ProfileContract) {
    this.view = view
  }
}