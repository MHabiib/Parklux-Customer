package com.future.pms.ui.scan

import com.future.pms.di.base.BaseView

interface ScanContract : BaseView {
  fun showProgress(show: Boolean)
  fun bookingSuccess(idBooking: String)
}
