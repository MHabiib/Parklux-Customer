package com.future.pms.home.injection

import com.future.pms.home.network.HomeApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HomeModule {
  @Provides fun provideHomeApi(retrofit: Retrofit): HomeApi {
    return retrofit.create(HomeApi::class.java)
  }
}