package com.future.pms.ui.history.view

import com.future.pms.model.history.History
import com.future.pms.ui.base.BaseView

interface HistoryContract : BaseView {
  fun loadCustomerBookingSuccess(history: History)
  fun loadCustomerBookingError()
}