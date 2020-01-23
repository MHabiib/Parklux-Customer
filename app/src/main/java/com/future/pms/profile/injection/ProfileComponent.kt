package com.future.pms.profile.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.profile.view.ProfileFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ProfileModule::class])
interface ProfileComponent {
  fun inject(profileFragment: ProfileFragment)
}