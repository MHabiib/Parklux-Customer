package com.future.pms.ui.profile

import com.future.pms.ui.base.BaseContract

class ProfileContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun loadMessageSuccess(message: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadMessage()
    }
}