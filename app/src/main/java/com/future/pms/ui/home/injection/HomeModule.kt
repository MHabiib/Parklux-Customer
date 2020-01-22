package com.future.pms.ui.home.injection

import com.future.pms.di.scope.PerApplication
import com.future.pms.network.ApiServiceInterface
import com.future.pms.ui.home.network.IHomeApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HomeModule {
  @Provides @PerApplication fun provideCheckoutApi(retrofit: Retrofit): ApiServiceInterface {
    return retrofit.create(ApiServiceInterface::class.java)
  }

  @Provides fun provideHomeApi(retrofit: Retrofit): IHomeApi {
    return retrofit.create(IHomeApi::class.java)
  }
}