package com.future.pms.ui.ongoing.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.ongoing.view.OngoingFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [OngoingModule::class])
interface OngoingComponent {
  fun inject(ongoingFragment: OngoingFragment)
}