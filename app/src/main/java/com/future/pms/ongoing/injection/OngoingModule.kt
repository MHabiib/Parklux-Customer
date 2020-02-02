package com.future.pms.ongoing.injection

import com.future.pms.ongoing.network.OngoingApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class OngoingModule {
  @Provides fun provideOngoingApi(retrofit: Retrofit): OngoingApi {
    return retrofit.create(OngoingApi::class.java)
  }
}