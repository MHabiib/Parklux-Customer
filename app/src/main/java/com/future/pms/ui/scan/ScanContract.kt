package com.future.pms.ui.scan

import com.future.pms.di.base.BaseContract

interface ScanContract : BaseContract.View {
    fun showProgress(show: Boolean)
}
