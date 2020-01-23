package com.future.pms.bookingdetail.injection

import com.future.pms.bookingdetail.view.BookingDetailFragment
import com.future.pms.core.base.BaseComponent
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [BookingDetailModule::class])
interface BookingDetailComponent {
  fun inject(bookingDetailFragment: BookingDetailFragment)
}