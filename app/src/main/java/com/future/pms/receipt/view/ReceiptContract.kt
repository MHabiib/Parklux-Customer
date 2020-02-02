package com.future.pms.receipt.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Receipt

interface ReceiptContract : BaseView {
  fun showProgress(show: Boolean)
  fun loadReceiptSuccess(receipt: Receipt)
}