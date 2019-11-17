package com.future.pms.ui.scan

interface ScanContract {
    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
  fun bookingSuccess(idBooking: String)
  fun bookingFailed()
}
