package com.future.pms.ui.history.injection

import com.future.pms.ui.history.network.HistoryApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class HistoryModule {
  @Provides fun provideHomeApi(retrofit: Retrofit): HistoryApi {
    return retrofit.create(HistoryApi::class.java)
  }
}