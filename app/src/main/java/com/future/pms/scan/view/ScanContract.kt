package com.future.pms.scan.view

import com.future.pms.core.base.BaseView

interface ScanContract : BaseView {
  fun showProgress(show: Boolean)
  fun bookingSuccess(idBooking: String)
}
