package com.future.pms.ui.register.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.register.view.RegisterActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [RegisterModule::class])
interface RegisterComponent {
  fun inject(registerActivity: RegisterActivity)
}