package com.future.pms.ui.superadmin.listuser.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.listuser.view.ListUserFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ListUserModule::class])
interface ListUserComponent {
  fun inject(listUserFragment: ListUserFragment)
}