package com.future.pms.ui.superadmin.activitydetails

import com.future.pms.di.base.BaseView
import com.future.pms.model.receipt.Receipt

interface ActivityDetailsContract : BaseView {
  fun bookingReceiptSASuccess(receipt: Receipt)
  fun checkoutBookingSASuccess(receipt: Receipt?)
  fun showProgress(show: Boolean)
}