package com.future.pms.di.module

import android.app.Application
import com.future.pms.BaseApp
import com.future.pms.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}