package com.future.pms.ui.parkingdirection.injection

import com.future.pms.ui.parkingdirection.network.ParkingDirectionApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ParkingDirectionModule {
  @Provides fun provideParkingDirectionApi(retrofit: Retrofit): ParkingDirectionApi {
    return retrofit.create(ParkingDirectionApi::class.java)
  }
}