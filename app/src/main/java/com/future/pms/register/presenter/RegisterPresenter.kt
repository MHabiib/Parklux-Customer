package com.future.pms.register.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Customer
import com.future.pms.register.network.RegisterApi
import com.future.pms.register.view.RegisterContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor() : BasePresenter<RegisterContract>() {
  @Inject lateinit var registerApi: RegisterApi

  fun register(name: String, email: String, password: String, phoneNumber: String) {
    val customer = Customer(email, name, password, phoneNumber)
    subscriptions.add(
        registerApi.postCreateCustomer(customer).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({
          view?.onSuccess()
        }, {
          view?.onFailed(it.message.toString())
        }))
  }
}