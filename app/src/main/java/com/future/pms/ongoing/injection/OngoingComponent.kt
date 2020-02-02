package com.future.pms.ongoing.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.ongoing.view.OngoingFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [OngoingModule::class])
interface OngoingComponent {
  fun inject(ongoingFragment: OngoingFragment)
}