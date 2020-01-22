package com.future.pms.ui.scan.view

import com.future.pms.ui.base.BaseView

interface ScanContract : BaseView {
  fun showProgress(show: Boolean)
  fun bookingSuccess(idBooking: String)
}
