package com.future.pms.ui.main.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.main.view.MainActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [MainModule::class])
interface MainComponent {
  fun inject(mainActivity: MainActivity)
}