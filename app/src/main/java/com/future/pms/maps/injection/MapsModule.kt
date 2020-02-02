package com.future.pms.maps.injection

import com.future.pms.maps.network.MapsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class MapsModule {
  @Provides fun provideLoginApi(retrofit: Retrofit): MapsApi {
    return retrofit.create(MapsApi::class.java)
  }
}