package com.future.pms.ui.history

import com.future.pms.model.customerbooking.CustomerBooking

interface HistoryContract {
    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
    fun loadCustomerBookingSuccess(list: List<CustomerBooking>)
    fun refreshPage()
}