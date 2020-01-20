package com.future.pms.util

import io.reactivex.disposables.Disposable

interface RxUtils {

  companion object {
    fun isDisposableInitialized(disposable: Disposable?): Boolean {
      return disposable != null && !disposable.isDisposed
    }
  }
}
