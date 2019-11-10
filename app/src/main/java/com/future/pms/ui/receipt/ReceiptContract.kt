package com.future.pms.ui.receipt

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.receipt.Receipt

interface ReceiptContract : BaseMVPView {
    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
    fun loadReceiptSuccess(receipt: Receipt)
}