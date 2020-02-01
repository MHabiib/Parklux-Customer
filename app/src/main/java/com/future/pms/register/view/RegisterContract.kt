package com.future.pms.register.view

import com.future.pms.core.base.BaseView
import com.future.pms.core.model.Token

interface RegisterContract : BaseView {
  fun onSuccess(token: Token)
}