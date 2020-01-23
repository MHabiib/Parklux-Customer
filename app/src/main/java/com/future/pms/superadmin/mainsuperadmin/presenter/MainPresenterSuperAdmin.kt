package com.future.pms.superadmin.mainsuperadmin.presenter

import com.future.pms.superadmin.mainsuperadmin.view.MainContractSuperAdmin
import javax.inject.Inject

class MainPresenterSuperAdmin @Inject constructor() {
  private lateinit var view: MainContractSuperAdmin

  fun subscribe() {
    //No implement required
  }

  fun onHomeIconClick() {
    view.showHomeFragment()
  }

  fun attach(view: MainContractSuperAdmin) {
    this.view = view
    view.showHomeFragment()
  }

  fun onActivityClick() {
    view.showListActivityFragment()
  }

  fun onCustomerClick() {
    view.showListCustomerFragment()
  }
}