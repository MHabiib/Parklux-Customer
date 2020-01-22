package com.future.pms.ui.superadmin.listactivity.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.listactivity.view.ListActivityFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ListActivityModule::class])
interface ListActivityComponent {
  fun inject(listActivityFragment: ListActivityFragment)
}