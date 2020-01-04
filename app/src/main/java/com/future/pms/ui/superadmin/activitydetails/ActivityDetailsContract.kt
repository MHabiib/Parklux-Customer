package com.future.pms.ui.superadmin.activitydetails

import com.future.pms.model.receipt.Receipt

interface ActivityDetailsContract {
  fun showErrorMessage(error: String)
  fun bookingReceiptSASuccess(receipt: Receipt)
  fun checkoutBookingSASuccess(receipt: Receipt?)

}