package com.future.pms.model.oauth.request

import java.io.Serializable

data class Auth(
  val username: String, val password: String, val grant_type: String
) : Serializable
