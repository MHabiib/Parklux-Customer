package com.future.pms.core.base

import com.future.pms.BaseApp
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Component(modules = [BaseModule::class, BaseNetworkModule::class]) interface BaseComponent {
  fun inject(baseApp: BaseApp)

  fun gson(): Gson

  fun genericRetrofit(): Retrofit

  fun okHttpClient(): OkHttpClient
}