package com.future.pms.ui.main

interface MainContract {
  fun showHomeFragment()
  fun showScanFragment()
  fun showProfileFragment()
  fun showReceipt(idBooking: String)
  fun showParkingDirection()
  fun showBookingDetail(idBooking: String)
  fun showBookingFailed()
  fun showLoginPage()
}