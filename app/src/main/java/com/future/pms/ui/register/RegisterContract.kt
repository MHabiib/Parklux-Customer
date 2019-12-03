package com.future.pms.ui.register

import com.future.pms.di.base.BaseMVPView

interface RegisterContract : BaseMVPView {
  fun onSuccess()
  fun onFailed(e: String)
  fun onError(e: Throwable)
}