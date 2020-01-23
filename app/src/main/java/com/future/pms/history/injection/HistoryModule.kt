package com.future.pms.history.injection

import com.future.pms.history.network.HistoryApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HistoryModule {
  @Provides fun provideHistoryApi(retrofit: Retrofit): HistoryApi {
    return retrofit.create(HistoryApi::class.java)
  }
}