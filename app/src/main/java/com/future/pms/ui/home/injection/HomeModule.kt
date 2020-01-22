package com.future.pms.ui.home.injection

import com.future.pms.ui.history.network.HistoryApi
import com.future.pms.ui.home.network.HomeApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HomeModule {
  @Provides fun provideHomeApi(retrofit: Retrofit): HomeApi {
    return retrofit.create(HomeApi::class.java)
  }

  @Provides fun provideHistoryApi(retrofit: Retrofit): HistoryApi {
    return retrofit.create(HistoryApi::class.java)
  }
}