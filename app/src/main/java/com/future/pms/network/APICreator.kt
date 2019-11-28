package com.future.pms.network

import com.future.pms.network.NetworkConstant.BASE
import com.future.pms.network.NetworkConstant.PASSWORD
import com.future.pms.network.NetworkConstant.READ_TIMEOUT
import com.future.pms.network.NetworkConstant.USERNAME
import com.future.pms.network.NetworkConstant.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APICreator<out API>(
  private val clazz: Class<API>,
  private var headers: HashMap<String, String> = HashMap(),
  private var converterFactory: Converter.Factory? = GsonConverterFactory.create()
) {
  private fun getOkHttpBuilder(): OkHttpClient.Builder {
    return OkHttpClient.Builder().addInterceptor(
      BasicAuthInterceptor(
        USERNAME, PASSWORD
      )
    ).writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
  }

  fun generate(): API {
    val okHttpClient = getOkHttpBuilder()
    okHttpClient.addNetworkInterceptor { chain ->
      val original = chain.request()
      val requestBuilder = original.newBuilder()
      headers.map {
        requestBuilder.addHeader(it.key, it.value)
      }
      requestBuilder.method(original.method(), original.body())
      val request = requestBuilder.build()
      chain.proceed(request)
    }
    val client = okHttpClient.build()
    val retrofit =
      Retrofit.Builder().baseUrl(BASE).client(client).addConverterFactory(converterFactory!!)
        .build()
    return retrofit.create(clazz)
  }
}