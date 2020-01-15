package com.future.pms.ui.receipt

import com.future.pms.di.base.BaseView
import com.future.pms.model.receipt.Receipt

interface ReceiptContract : BaseView {
  fun showProgress(show: Boolean)
  fun loadReceiptSuccess(receipt: Receipt)
}