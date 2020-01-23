package com.future.pms.login.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.login.view.LoginActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [LoginModule::class])
interface LoginComponent {
  fun inject(loginActivity: LoginActivity)
}