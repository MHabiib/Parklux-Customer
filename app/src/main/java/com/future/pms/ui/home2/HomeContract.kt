package com.future.pms.ui.home2

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

object HomeContract {

    interface HomeView : BaseMVPView {
        fun onLogout()
    }

    interface HomePresenter : BaseMVPPresenter<HomeView> {
        fun signOut()
    }
}