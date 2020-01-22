package com.future.pms.util

import android.view.View
import io.reactivex.Observable
import io.reactivex.functions.Function

open class BaseErrorHandler {
  companion object {
    protected fun getDefaultRetryWhen(
        view: View): Function<Observable<out Throwable>, Observable<*>> {
      return getDefaultRetryWhen(view)
    }
  }
}