package com.future.pms.superadmin.listactivity.injection

import com.future.pms.superadmin.listactivity.network.ListActivityApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ListActivityModule {
  @Provides fun provideListActivityApi(retrofit: Retrofit): ListActivityApi {
    return retrofit.create(ListActivityApi::class.java)
  }
}