package com.future.pms.ui.bookingdetail.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.bookingdetail.view.BookingDetailFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [BookingDetailModule::class])
interface BookingDetailComponent {
  fun inject(bookingDetailFragment: BookingDetailFragment)
}