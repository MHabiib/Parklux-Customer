package com.future.pms.superadmin.mainsuperadmin.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.mainsuperadmin.view.MainActivitySuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class])
interface MainComponentSuperAdmin {
  fun inject(mainActivitySuperAdmin: MainActivitySuperAdmin)
}