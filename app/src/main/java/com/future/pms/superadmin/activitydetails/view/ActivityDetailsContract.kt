package com.future.pms.superadmin.activitydetails.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Receipt

interface ActivityDetailsContract : BaseView {
  fun bookingReceiptSASuccess(receipt: Receipt)
  fun checkoutBookingSASuccess(receipt: Receipt?)
  fun showProgress(show: Boolean)
}