package com.future.pms.ui.parkingdirection

import com.future.pms.ui.base.BaseView

interface ParkingDirectionContract : BaseView {
  fun showProgress(show: Boolean)
  fun getLayoutSuccess(slotsLayout: String)
}