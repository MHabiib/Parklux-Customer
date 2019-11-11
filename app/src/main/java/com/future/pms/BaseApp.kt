package com.future.pms

import android.app.Application
import androidx.multidex.BuildConfig
import com.future.pms.di.component.ApplicationComponent
import com.future.pms.di.component.DaggerApplicationComponent
import com.future.pms.di.module.ApplicationModule

class BaseApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        instance = this
        setup()
    }

    private fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}