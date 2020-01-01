package com.future.pms.di.component

import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.register.RegisterActivity
import com.future.pms.ui.splash.SplashActivity
import com.future.pms.ui.superadmin.loginsuperadmin.LoginActivitySuperAdmin
import com.future.pms.ui.superadmin.mainsuperadmin.MainActivitySuperAdmin
import dagger.Component

@Component(modules = [ActivityModule::class]) interface ActivityComponent {
  fun inject(mainActivity: MainActivity)

  fun inject(mainActivitySuperAdmin: MainActivitySuperAdmin)

  fun inject(splashActivity: SplashActivity)

  fun inject(loginActivity: LoginActivity)

  fun inject(loginActivitySuperAdminSuperAdmin: LoginActivitySuperAdmin)

  fun inject(registerActivity: RegisterActivity)
}