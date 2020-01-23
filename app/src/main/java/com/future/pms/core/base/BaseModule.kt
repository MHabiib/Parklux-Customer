package com.future.pms.core.base

import android.content.Context
import com.future.pms.BaseApp
import dagger.Module
import dagger.Provides

@Module class BaseModule(private val baseApplication: BaseApp) {
  @Provides fun provideContext(): Context {
    return baseApplication.applicationContext
  }
}