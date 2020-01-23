package com.future.pms.register.injection

import com.future.pms.register.network.RegisterApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class RegisterModule {
  @Provides fun provideRegisterApi(retrofit: Retrofit): RegisterApi {
    return retrofit.create(RegisterApi::class.java)
  }
}