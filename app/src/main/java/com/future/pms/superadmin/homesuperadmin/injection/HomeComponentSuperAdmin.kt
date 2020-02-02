package com.future.pms.superadmin.homesuperadmin.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.homesuperadmin.view.HomeFragmentSuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [HomeModuleSuperAdmin::class])
interface HomeComponentSuperAdmin {
  fun inject(homeFragmentSuperAdmin: HomeFragmentSuperAdmin)
}