package com.future.pms.ui.login.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.login.view.LoginActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [LoginModule::class])
interface LoginComponent {
  fun inject(loginActivity: LoginActivity)
}