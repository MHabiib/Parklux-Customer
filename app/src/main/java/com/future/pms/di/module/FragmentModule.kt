package com.future.pms.di.module

import androidx.fragment.app.Fragment
import com.future.pms.ui.parkingdirection.presenter.ParkingDirectionPresenter
import com.future.pms.ui.profile.ProfilePresenter
import com.future.pms.ui.receipt.ReceiptPresenter
import com.future.pms.ui.scan.ScanPresenter
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

  @Provides fun provideParkingDirectionPresenter(): ParkingDirectionPresenter {
    return ParkingDirectionPresenter()
  }

  @Provides fun provideProfilePresenter(): ProfilePresenter {
    return ProfilePresenter()
  }

  @Provides fun provideReceiptPresenter(): ReceiptPresenter {
    return ReceiptPresenter()
  }

  @Provides fun provideScanPresenter(): ScanPresenter {
    return ScanPresenter()
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