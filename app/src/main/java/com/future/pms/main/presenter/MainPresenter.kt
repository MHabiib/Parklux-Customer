package com.future.pms.main.presenter

import com.future.pms.main.view.MainContract
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

  fun showBookingDetail(idBooking: String) {
    view.showBookingDetail(idBooking)
  }

  fun showBookingFailed() {
    view.showBookingFailed()
  }

  fun showParkingDirection(idBooking: String, levelName: String) {
    view.showParkingDirection(idBooking, levelName)
  }

  fun attach(view: MainContract) {
    this.view = view
    view.showHomeFragment()
  }
}