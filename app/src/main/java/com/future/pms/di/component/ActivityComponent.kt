package com.future.pms.di.component

import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.register.RegisterActivity
import com.future.pms.ui.splash.SplashActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class)) interface ActivityComponent {
  fun inject(mainActivity: MainActivity)

  fun inject(splashActivity: SplashActivity)

  fun inject(loginActivity: LoginActivity)

  fun inject(registerActivity: RegisterActivity)
}