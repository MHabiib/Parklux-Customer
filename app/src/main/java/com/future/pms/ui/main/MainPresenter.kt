package com.future.pms.ui.main

import javax.inject.Inject

class MainPresenter @Inject constructor() {
  private lateinit var view: MainContract

  fun subscribe() {
    //No implement required
  }

  fun onHomeIconClick() {
    view.showHomeFragment()
  }

  fun onScanIconClick() {
    view.showScanFragment()
  }

  fun onProfileIconClick() {
    view.showProfileFragment()
  }

  fun showReceipt(idBooking: String) {
    view.showReceipt(idBooking)
  }

  fun showBookingDetail(idBooking: String) {
    view.showBookingDetail(idBooking)
  }

  fun showBookingFailed() {
    view.showBookingFailed()
  }

  fun showParkingDirection(idBooking: String) {
    view.showParkingDirection(idBooking)
  }

  fun showLoginPage() {
    view.showLoginPage()
  }

  fun attach(view: MainContract) {
    this.view = view
    view.showHomeFragment()
  }
}