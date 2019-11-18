package com.future.pms.model.register

data class CustomerRequest(
  val email: String, val name: String, val password: String, val phoneNumber: String
)