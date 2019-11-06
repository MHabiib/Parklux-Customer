package com.future.pms.ui.profile

import com.future.pms.di.base.BaseMVPPresenter
import com.future.pms.di.base.BaseMVPView

class ProfileContract {

    interface View : BaseMVPView {
        fun showProgress(show: Boolean)
        fun onLogout()
    }

    interface Presenter : BaseMVPPresenter<View> {
        fun signOut()
    }
}