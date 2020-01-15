package com.future.pms.ui.history

import com.future.pms.di.base.BaseView
import com.future.pms.model.history.History

interface HistoryContract : BaseView {
  fun loadCustomerBookingSuccess(history: History)
  fun loadCustomerBookingError()
}