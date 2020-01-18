package com.future.pms.di.module

import com.future.pms.util.Constants.Companion.BASE
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module class NetworkModule {

  @Singleton @Provides fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().baseUrl(BASE).addCallAdapterFactory(
        RxJava2CallAdapterFactory.create()).addConverterFactory(
        GsonConverterFactory.create(GsonBuilder().setLenient().create())).build()
  }
}