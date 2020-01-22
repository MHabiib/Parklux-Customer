package com.future.pms.di.module

import com.future.pms.BaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class]) class ApplicationModule(private val baseApp: BaseApp) {
  @Provides @Singleton fun provideApplication(): BaseApp {
    return baseApp
  }
}