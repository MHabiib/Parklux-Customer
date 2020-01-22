package com.future.pms.ui.superadmin.homesuperadmin.injection

import com.future.pms.ui.superadmin.homesuperadmin.network.HomeApiSuperAdmin
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HomeModuleSuperAdmin {
  @Provides fun provideHomeApiSuperAdmin(retrofit: Retrofit): HomeApiSuperAdmin {
    return retrofit.create(HomeApiSuperAdmin::class.java)
  }
}