package com.future.pms.network

import com.future.pms.util.Constants.Companion.BASE
import com.future.pms.util.Constants.Companion.PASSWORD
import com.future.pms.util.Constants.Companion.READ_TIMEOUT
import com.future.pms.util.Constants.Companion.USERNAME
import com.future.pms.util.Constants.Companion.WRITE_TIMEOUT
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APICreator<out API>(private val clazz: Class<API>,
    private var headers: HashMap<String, String> = HashMap()) {

  companion object Factory {
    fun create(): ApiServiceInterface {
      val retrofit = retrofit2.Retrofit.Builder().baseUrl(BASE).addCallAdapterFactory(
          RxJava2CallAdapterFactory.create()).addConverterFactory(
          GsonConverterFactory.create(GsonBuilder().setLenient().create())).build()

      return retrofit.create(ApiServiceInterface::class.java)
    }
  }

  private fun getOkHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder().addInterceptor(
        BasicAuthInterceptor(USERNAME, PASSWORD)).writeTimeout(WRITE_TIMEOUT,
        TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
  }

  fun generate(): API {
    val okHttpClient = getOkHttpBuilder()
    okHttpClient.addNetworkInterceptor { chain ->
      val original = chain.request()
      val requestBuilder = original.newBuilder()
      headers.map {
        requestBuilder.addHeader(it.key, it.value)
      }
      requestBuilder.method(original.method, original.body)
      val request = requestBuilder.build()
      chain.proceed(request)
    }
    val client = okHttpClient.build()
    val retrofit = Retrofit.Builder().baseUrl(BASE).client(client).addCallAdapterFactory(
        RxJava2CallAdapterFactory.create()).addConverterFactory(
            GsonConverterFactory.create()).build()
    return retrofit.create(clazz)
  }
}