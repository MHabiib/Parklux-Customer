package com.future.pms.ui.home

import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.customerdetail.Customer

interface HomeContract {

    fun showProgress(show: Boolean)
    fun showErrorMessage(error: String)
    fun loadCustomerBookingSuccess(list: List<CustomerBooking>)
    fun loadCustomerDetailSuccess(customer: Customer)
    fun showParkingDirectionFragment()
    fun getDateNow()
}