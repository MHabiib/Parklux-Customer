package com.future.pms.network

import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APICreator<out API>(
    private val clazz: Class<API>,
    private val baseUrl: String,
    private var writeTimeout: Long = 30,
    private var readTimeout: Long = 30,
    private var headers: HashMap<String, String> = HashMap(),
    private var converterFactory: Converter.Factory? = GsonConverterFactory.create()) {

    private fun getOkHttpBuilder(writeTimeout: Long, readTimeout: Long): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(
                BasicAuthInterceptor(
                    "pms-client",
                    "pms-secret"
                )
            )
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
    }

    fun generate(): API {
        val okHttpClient = getOkHttpBuilder(writeTimeout, readTimeout)

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
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory!!)
            .build()
        return retrofit.create(clazz)
    }
}