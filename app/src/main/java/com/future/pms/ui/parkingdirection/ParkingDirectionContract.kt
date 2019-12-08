package com.future.pms.ui.parkingdirection

interface ParkingDirectionContract {
  fun showProgress(show: Boolean)
  fun getLayoutSuccess(slotsLayout: String)
  fun getLayoutFailed()
}