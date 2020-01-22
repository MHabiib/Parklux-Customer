package com.future.pms.di.module

import androidx.fragment.app.Fragment
import com.future.pms.ui.superadmin.activitydetails.presenter.ActivityDetailsPresenter
import com.future.pms.ui.superadmin.homesuperadmin.presenter.HomePresenterSuperAdmin
import com.future.pms.ui.superadmin.listactivity.presenter.ListActivityPresenter
import com.future.pms.ui.superadmin.listuser.presenter.ListUserPresenter
import com.future.pms.ui.superadmin.userdetails.presenter.UserDetailsPresenter
import dagger.Module
import dagger.Provides

@Module class FragmentModule(private var fragment: Fragment) {
  @Provides fun provideFragment(): Fragment {
    return fragment
  }

  @Provides fun provideActivityDetailsPresenter(): ActivityDetailsPresenter {
    return ActivityDetailsPresenter()
  }

  @Provides fun provideHomePresenterSuperAdmin(): HomePresenterSuperAdmin {
    return HomePresenterSuperAdmin()
  }

  @Provides fun provideListActivityPresenter(): ListActivityPresenter {
    return ListActivityPresenter()
  }

  @Provides fun provideListUserPresenter(): ListUserPresenter {
    return ListUserPresenter()
  }

  @Provides fun provideUserDetailsPresenter(): UserDetailsPresenter {
    return UserDetailsPresenter()
  }
}