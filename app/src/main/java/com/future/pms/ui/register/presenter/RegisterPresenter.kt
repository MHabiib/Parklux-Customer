package com.future.pms.ui.register.presenter

import com.future.pms.model.register.CustomerRequest
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.register.network.RegisterApi
import com.future.pms.ui.register.view.RegisterContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor() : BasePresenter<RegisterContract>() {
  @Inject lateinit var registerApi: RegisterApi

  fun register(name: String, email: String, password: String, phoneNumber: String) {
    val customer = CustomerRequest(email, name, password, phoneNumber)
    subscriptions.add(
        registerApi.postCreateCustomer(customer).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.onSuccess()
    }, {
      view?.onFailed(it.message.toString())
        }))
  }
}