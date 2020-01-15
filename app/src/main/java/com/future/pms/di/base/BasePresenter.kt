package com.future.pms.di.base

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<V : BaseView> {
  protected val api: ApiServiceInterface = RetrofitClient.create()
  protected var subscriptions = CompositeDisposable()
  protected var view: V? = null

  fun subscribe() {}

  protected fun getContext(): Context? {
    return when (view) {
      is Fragment -> (view as Fragment).context
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }
}