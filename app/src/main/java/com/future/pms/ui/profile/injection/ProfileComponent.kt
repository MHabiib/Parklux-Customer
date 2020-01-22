package com.future.pms.ui.profile.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.profile.view.ProfileFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ProfileModule::class])
interface ProfileComponent {
  fun inject(profileFragment: ProfileFragment)
}