package com.future.pms.di.component

import com.future.pms.BaseApp
import com.future.pms.di.module.ApplicationModule
import dagger.Component

@Component(modules = [ApplicationModule::class]) interface ApplicationComponent {
  fun inject(application: BaseApp)
}