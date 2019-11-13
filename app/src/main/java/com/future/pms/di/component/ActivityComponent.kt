package com.future.pms.di.component

import com.future.pms.di.module.ActivityModule
import com.future.pms.ui.main.MainActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)
}