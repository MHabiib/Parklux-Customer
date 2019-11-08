package com.future.pms.ui.parkingDirection

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

class ParkingDirectionContract {

    interface View : BaseMVPView {
        fun showProgress(show: Boolean)
        fun onParkingSlotView(seats: String)
    }

    interface Presenter : BaseMVPPresenter<View> {
        fun onParkingSlotClick(view: View)
        fun getParkingSlotData()
    }
}