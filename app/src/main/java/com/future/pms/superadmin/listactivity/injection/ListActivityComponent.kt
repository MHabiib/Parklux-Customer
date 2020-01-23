package com.future.pms.superadmin.listactivity.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.listactivity.view.ListActivityFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ListActivityModule::class])
interface ListActivityComponent {
  fun inject(listActivityFragment: ListActivityFragment)
}