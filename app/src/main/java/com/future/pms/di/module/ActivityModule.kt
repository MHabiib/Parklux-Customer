package com.future.pms.di.module

import android.app.Activity
import com.future.pms.ui.main.MainContract
import com.future.pms.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private var activity: Activity) {

    @Provides
    fun provideActivity(): Activity {
        return activity
    }

    @Provides
    fun providePresenter(): MainPresenter {
        return MainPresenter()
    }

}