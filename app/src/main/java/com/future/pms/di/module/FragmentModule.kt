package com.future.pms.di.module

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import com.future.pms.ui.home.HomeContract
import com.future.pms.ui.home.HomePresenter
import com.future.pms.ui.profile.ProfileContract
import com.future.pms.ui.profile.ProfilePresenter
import com.future.pms.ui.scan.ScanContract
import com.future.pms.ui.scan.ScanPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    @Provides
    fun provideHomePresenter(): HomeContract.Presenter {
        return HomePresenter()
    }

    @Provides
    fun provideScanPresenter(): ScanContract.Presenter {
        return ScanPresenter()
    }

    @Provides
    fun provideProfilePresenter(): ProfileContract.Presenter {
        return ProfilePresenter()
    }

    @Provides
    fun provideApiService(): ApiServiceInterface {
        return RetrofitClient.create()
    }
}