package com.future.pms.register.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.register.view.RegisterActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [RegisterModule::class])
interface RegisterComponent {
  fun inject(registerActivity: RegisterActivity)
}