package com.future.pms.ui.parkingdirection

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

interface ParkingDirectionContract : BaseMVPView {

    fun showProgress(show: Boolean)
}