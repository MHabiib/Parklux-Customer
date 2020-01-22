package com.future.pms.ui.bookingdetail.injection

import com.future.pms.ui.bookingdetail.network.BookingDetailApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class BookingDetailModule {
  @Provides fun provideBookingDetailApi(retrofit: Retrofit): BookingDetailApi {
    return retrofit.create(BookingDetailApi::class.java)
  }
}