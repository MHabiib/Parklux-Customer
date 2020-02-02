package com.future.pms.superadmin.activitydetails.injection

import com.future.pms.superadmin.activitydetails.network.ActivityDetailsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ActivityDetailsModule {
  @Provides fun provideActivityDetailsApi(retrofit: Retrofit): ActivityDetailsApi {
    return retrofit.create(ActivityDetailsApi::class.java)
  }
}