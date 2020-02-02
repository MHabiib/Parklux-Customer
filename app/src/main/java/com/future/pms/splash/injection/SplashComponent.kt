package com.future.pms.splash.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.splash.view.SplashActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [SplashModule::class])
interface SplashComponent {
  fun inject(splashActivity: SplashActivity)
}