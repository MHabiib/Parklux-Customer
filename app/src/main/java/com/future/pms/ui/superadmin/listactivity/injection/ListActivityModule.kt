package com.future.pms.ui.superadmin.listactivity.injection

import com.future.pms.ui.superadmin.listactivity.network.ListActivityApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ListActivityModule {
  @Provides fun provideListActivityApi(retrofit: Retrofit): ListActivityApi {
    return retrofit.create(ListActivityApi::class.java)
  }
}