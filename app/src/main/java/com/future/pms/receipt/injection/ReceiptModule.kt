package com.future.pms.receipt.injection

import com.future.pms.receipt.network.ReceiptApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ReceiptModule {
  @Provides fun provideReceiptApi(retrofit: Retrofit): ReceiptApi {
    return retrofit.create(ReceiptApi::class.java)
  }
}