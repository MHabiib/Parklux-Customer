package com.future.pms.di.module

import android.app.Activity
import com.future.pms.ui.splash.SplashPresenter
import com.future.pms.ui.superadmin.loginsuperadmin.LoginPresenterSuperAdmin
import com.future.pms.ui.superadmin.mainsuperadmin.MainPresenterSuperAdmin
import dagger.Module
import dagger.Provides

@Module class ActivityModule(private var activity: Activity) {
  @Provides fun provideActivity(): Activity {
    return activity
  }

  @Provides fun provideSplashPresenter(): SplashPresenter {
    return SplashPresenter()
  }

  @Provides fun provideLoginPresenterSuperAdmin(): LoginPresenterSuperAdmin {
    return LoginPresenterSuperAdmin()
  }

  @Provides fun provideMainPresenterSuperAdmin(): MainPresenterSuperAdmin {
    return MainPresenterSuperAdmin()
  }
}