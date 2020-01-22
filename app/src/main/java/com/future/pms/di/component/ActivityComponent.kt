package com.future.pms.di.component

import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.splash.view.SplashActivity
import com.future.pms.ui.superadmin.loginsuperadmin.view.LoginActivitySuperAdmin
import com.future.pms.ui.superadmin.mainsuperadmin.view.MainActivitySuperAdmin
import dagger.Component

@Component(modules = [ActivityModule::class]) interface ActivityComponent {

  fun inject(mainActivitySuperAdmin: MainActivitySuperAdmin)

  fun inject(splashActivity: SplashActivity)

  fun inject(loginActivitySuperAdminSuperAdmin: LoginActivitySuperAdmin)
}