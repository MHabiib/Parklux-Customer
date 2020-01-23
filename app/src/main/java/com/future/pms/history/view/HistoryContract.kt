package com.future.pms.history.view

import com.future.pms.core.base.BaseView
import com.future.pms.history.model.History

interface HistoryContract : BaseView {
  fun loadCustomerBookingSuccess(history: History)
  fun loadCustomerBookingError()
}