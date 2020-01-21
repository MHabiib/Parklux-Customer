package com.future.pms.ui.home

import com.future.pms.di.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module
abstract class HomeModule {
    @ActivityScope
    @Binds
    abstract fun provideHomeView(homeFragment: HomeFragment): HomeContract
}