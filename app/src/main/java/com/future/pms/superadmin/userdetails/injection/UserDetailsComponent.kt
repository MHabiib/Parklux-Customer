package com.future.pms.superadmin.userdetails.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.userdetails.view.UserDetailsFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [UserDetailsModule::class])
interface UserDetailsComponent {
  fun inject(userDetailsFragment: UserDetailsFragment)
}