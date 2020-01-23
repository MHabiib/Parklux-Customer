package com.future.pms.login.injection

import com.future.pms.login.network.LoginApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class LoginModule {
  @Provides fun provideLoginApi(retrofit: Retrofit): LoginApi {
    return retrofit.create(LoginApi::class.java)
  }
}