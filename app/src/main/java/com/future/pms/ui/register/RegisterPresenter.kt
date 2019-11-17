package com.future.pms.ui.register

import javax.inject.Inject

class RegisterPresenter @Inject constructor() {
  private lateinit var view: RegisterContract

  fun subscribe() {}

  fun attach(view: RegisterContract) {
    this.view = view
  }
}