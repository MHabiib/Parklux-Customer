package com.future.pms.scan.injection

import com.future.pms.scan.network.ScanApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ScanModule {
  @Provides fun provideScanApi(retrofit: Retrofit): ScanApi {
    return retrofit.create(ScanApi::class.java)
  }
}