package com.future.pms.ui.superadmin.homesuperadmin.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.homesuperadmin.view.HomeFragmentSuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HomeModuleSuperAdmin::class])
interface HomeComponentSuperAdmin {
  fun inject(homeFragmentSuperAdmin: HomeFragmentSuperAdmin)
}