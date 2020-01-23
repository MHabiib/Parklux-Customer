package com.future.pms.home.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.home.view.HomeFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HomeModule::class])
interface HomeComponent {
  fun inject(homeFragment: HomeFragment)
}