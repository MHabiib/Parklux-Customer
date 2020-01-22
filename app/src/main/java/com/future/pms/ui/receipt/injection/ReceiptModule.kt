package com.future.pms.ui.receipt.injection

import com.future.pms.ui.receipt.network.ReceiptApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ReceiptModule {
  @Provides fun provideReceiptApi(retrofit: Retrofit): ReceiptApi {
    return retrofit.create(ReceiptApi::class.java)
  }
}