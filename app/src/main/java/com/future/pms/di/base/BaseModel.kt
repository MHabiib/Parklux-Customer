package com.future.pms.di.base

import io.reactivex.disposables.CompositeDisposable

open class BaseModel {
  protected var mSubscriptions: CompositeDisposable = CompositeDisposable()
}