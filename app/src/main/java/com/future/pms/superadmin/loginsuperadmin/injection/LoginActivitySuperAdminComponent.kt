package com.future.pms.superadmin.loginsuperadmin.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.superadmin.loginsuperadmin.view.LoginActivitySuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [LoginActivitySuperAdminModule::class])
interface LoginActivitySuperAdminComponent {
  fun inject(loginActivitySuperAdmin: LoginActivitySuperAdmin)
}