package com.future.pms.ui.home.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.home.view.HomeFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HomeModule::class])
interface HomeComponent {
  fun inject(homeFragment: HomeFragment)

}