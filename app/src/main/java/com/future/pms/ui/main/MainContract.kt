package com.future.pms.ui.main

import com.future.pms.di.base.BaseContract

class MainContract {

    interface View: BaseContract.View {
        fun showHomeFragment()
        fun showScanFragment()
        fun showProfileFragment()
    }

    interface Presenter: BaseContract.Presenter<View> {
        fun onHomeIconClick()
        fun onScanIconClick()
        fun onProfileIconClick()
    }
}