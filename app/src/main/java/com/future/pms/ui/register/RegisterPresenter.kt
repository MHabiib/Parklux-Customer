package com.future.pms.ui.register

import com.future.pms.di.base.BasePresenter
import com.future.pms.model.register.CustomerRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor() : BasePresenter<RegisterContract>() {

  fun attach(view: RegisterContract) {
    this.view = view
  }

  fun register(name: String, email: String, password: String, phoneNumber: String) {
    val customer = CustomerRequest(email, name, password, phoneNumber)
    val subscribe = api.postCreateCustomer(customer).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.onSuccess()
    }, {
      view?.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }
}