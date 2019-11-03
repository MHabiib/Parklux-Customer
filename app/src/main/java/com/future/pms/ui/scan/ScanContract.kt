package com.future.pms.ui.scan

import com.future.pms.ui.base.BaseContract

class ScanContract {

    interface View : BaseContract.View {
        fun showProgress(show: Boolean)
        fun loadMessageSuccess(message: String)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun scanBarcode()
    }
}