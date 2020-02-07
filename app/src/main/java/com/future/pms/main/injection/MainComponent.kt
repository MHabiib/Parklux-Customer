package com.future.pms.main.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.main.view.MainActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class])
interface MainComponent {
  fun inject(mainActivity: MainActivity)
}