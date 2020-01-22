package com.future.pms.ui.superadmin.loginsuperadmin.injection

import com.future.pms.ui.superadmin.loginsuperadmin.network.LoginSuperAdminApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class LoginActivitySuperAdminModule {
  @Provides fun provideLoginSuperAdminApi(retrofit: Retrofit): LoginSuperAdminApi {
    return retrofit.create(LoginSuperAdminApi::class.java)
  }
}