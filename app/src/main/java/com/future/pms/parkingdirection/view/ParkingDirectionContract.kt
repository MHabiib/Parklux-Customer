package com.future.pms.parkingdirection.view

import com.future.pms.core.base.BaseView

interface ParkingDirectionContract : BaseView {
  fun showProgress(show: Boolean)
  fun getLayoutSuccess(slotsLayout: String)
}