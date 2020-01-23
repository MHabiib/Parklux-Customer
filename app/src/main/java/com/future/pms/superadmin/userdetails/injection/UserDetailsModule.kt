package com.future.pms.superadmin.userdetails.injection

import com.future.pms.superadmin.userdetails.network.UserDetailsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class UserDetailsModule {
  @Provides fun provideUserDetailsApi(retrofit: Retrofit): UserDetailsApi {
    return retrofit.create(UserDetailsApi::class.java)
  }
}