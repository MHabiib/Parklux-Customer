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

    fun showReceipt() {
        view.showReceipt()
    }

    fun showParkingDirection() {
        view.showParkingDirection()
    }

    fun attach(view: MainContract) {
        this.view = view
        view.showHomeFragment()
    }
}