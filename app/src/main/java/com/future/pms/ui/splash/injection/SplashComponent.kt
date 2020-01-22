package com.future.pms.ui.splash.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.splash.view.SplashActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [SplashModule::class])
interface SplashComponent {
  fun inject(splashActivity: SplashActivity)
}