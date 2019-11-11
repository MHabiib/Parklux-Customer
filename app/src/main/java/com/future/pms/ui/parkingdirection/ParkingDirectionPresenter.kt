package com.future.pms.ui.parkingdirection

import javax.inject.Inject

class ParkingDirectionPresenter @Inject constructor() {

    private lateinit var view: ParkingDirectionContract

    fun attach(view: ParkingDirectionContract) {
        this.view = view
    }

    fun subscribe() {}
}