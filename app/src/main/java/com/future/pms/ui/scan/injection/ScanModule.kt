package com.future.pms.ui.scan.injection

import com.future.pms.ui.scan.network.ScanApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ScanModule {
  @Provides fun provideScanApi(retrofit: Retrofit): ScanApi {
    return retrofit.create(ScanApi::class.java)
  }
}