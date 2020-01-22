package com.future.pms.ui.superadmin.activitydetails.injection

import com.future.pms.ui.superadmin.activitydetails.network.ActivityDetailsApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ActivityDetailsModule {
  @Provides fun provideActivityDetailsApi(retrofit: Retrofit): ActivityDetailsApi {
    return retrofit.create(ActivityDetailsApi::class.java)
  }
}