package com.future.pms.di.module

import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {
    @Provides
    fun provideApiService(): ApiServiceInterface {
        return RetrofitClient.create()
    }
}