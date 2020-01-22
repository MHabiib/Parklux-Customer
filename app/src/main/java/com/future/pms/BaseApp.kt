package com.future.pms

import android.app.Application
import com.future.pms.di.component.ApplicationComponent
import com.future.pms.di.component.DaggerApplicationComponent
import com.future.pms.di.module.ApplicationModule
import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.base.BaseModule
import com.future.pms.ui.base.DaggerBaseComponent
import timber.log.Timber

class BaseApp : Application() {

  lateinit var component: ApplicationComponent
  lateinit var baseComponent: BaseComponent

  companion object {
    lateinit var instance: BaseApp
  }

  override fun onCreate() {
    super.onCreate()
    baseComponent = DaggerBaseComponent.builder().baseModule(BaseModule(this)).build()
    baseComponent.inject(this)
    instance = this
    setup()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  private fun setup() {
    component = DaggerApplicationComponent.builder().applicationModule(
        ApplicationModule(this)).build()
    component.inject(this)
  }
}