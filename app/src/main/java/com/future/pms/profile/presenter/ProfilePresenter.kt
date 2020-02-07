package com.future.pms.profile.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Customer
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.profile.network.ProfileApi
import com.future.pms.profile.view.ProfileContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor(private val profileApi: ProfileApi) :
    BasePresenter<ProfileContract>() {

  fun loadData(accessToken: String) {
    subscriptions.add(
        profileApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
          view?.loadCustomerDetailSuccess(customer)
        }, {
          view?.onFailed(it.message.toString())
        }))
  }

  fun update(name: String, email: String, password: String, phoneNumber: String, token: String) {
    view?.apply {
      val customer = Customer(email, name, password, phoneNumber)
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
}