package com.future.pms.ui.superadmin.mainsuperadmin.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.mainsuperadmin.view.MainActivitySuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [MainModuleSuperAdmin::class])
interface MainComponentSuperAdmin {
  fun inject(mainActivitySuperAdmin: MainActivitySuperAdmin)
}