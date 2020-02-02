package com.future.pms.superadmin.listuser.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.listuser.view.ListUserFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ListUserModule::class])
interface ListUserComponent {
  fun inject(listUserFragment: ListUserFragment)
}