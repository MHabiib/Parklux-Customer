package com.future.pms.ui.history

import com.future.pms.model.history.History

interface HistoryContract {
  fun showErrorMessage(error: String)
  fun loadCustomerBookingSuccess(history: History)
  fun loadCustomerBookingError()
}