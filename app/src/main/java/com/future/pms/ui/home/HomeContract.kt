package com.future.pms.ui.home

import com.future.pms.model.customerdetail.Customer

interface HomeContract {
    fun showErrorMessage(error: String)
    fun loadCustomerDetailSuccess(customer: Customer)
    fun getDateNow()
  fun unauthorized()
}