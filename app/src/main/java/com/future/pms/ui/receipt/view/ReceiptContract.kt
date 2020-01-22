package com.future.pms.ui.receipt.view

import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.base.BaseView

interface ReceiptContract : BaseView {
  fun showProgress(show: Boolean)
  fun loadReceiptSuccess(receipt: Receipt)
}