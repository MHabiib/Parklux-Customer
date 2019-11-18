package com.future.pms.ui.register

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor() {
  private lateinit var view: RegisterContract

  fun subscribe() {}

  fun attach(view: RegisterContract) {
    this.view = view
  }

  fun register(name: String, email: String, password: String, phone: String) {
    val subscribe = api.postCreateUser().subscribeOn(Schedulers.io()).observeOn(
      AndroidSchedulers.mainThread()
    ).subscribe({}, {
      view.showErrorMessage(it.message.toString())
    })
    subscriptions.add(subscribe)
  }
}