package com.future.pms.di.module

import androidx.fragment.app.Fragment
import com.future.pms.ui.superadmin.activitydetails.ActivityDetailsPresenter
import com.future.pms.ui.superadmin.homesuperadmin.HomePresenterSuperAdmin
import com.future.pms.ui.superadmin.listactivity.ListActivityPresenter
import com.future.pms.ui.superadmin.listuser.ListUserPresenter
import com.future.pms.ui.superadmin.userdetails.UserDetailsPresenter
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