package com.future.pms.network

import com.google.gson.GsonBuilder
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
  companion object Factory {
    fun create(): ApiServiceInterface {
      val retrofit = retrofit2.Retrofit.Builder().baseUrl(APISettings.base)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(UnsafeOkHttpClient.unsafeOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

      return retrofit.create(ApiServiceInterface::class.java)
    }
  }
}