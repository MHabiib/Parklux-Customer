package com.future.pms.ui.superadmin.activitydetails

import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.base.BaseView

interface ActivityDetailsContract : BaseView {
  fun bookingReceiptSASuccess(receipt: Receipt)
  fun checkoutBookingSASuccess(receipt: Receipt?)
  fun showProgress(show: Boolean)
}