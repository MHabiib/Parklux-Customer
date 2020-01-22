package com.future.pms.ui.profile.injection

import com.future.pms.ui.profile.network.ProfileApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ProfileModule {
  @Provides fun provideProfileApi(retrofit: Retrofit): ProfileApi {
    return retrofit.create(ProfileApi::class.java)
  }
}