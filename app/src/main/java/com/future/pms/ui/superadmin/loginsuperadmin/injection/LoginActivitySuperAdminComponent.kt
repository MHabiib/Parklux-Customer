package com.future.pms.ui.superadmin.loginsuperadmin.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.superadmin.loginsuperadmin.view.LoginActivitySuperAdmin
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [LoginActivitySuperAdminModule::class])
interface LoginActivitySuperAdminComponent {
  fun inject(loginActivitySuperAdmin: LoginActivitySuperAdmin)
}