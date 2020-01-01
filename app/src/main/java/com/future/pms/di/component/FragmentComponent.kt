package com.future.pms.di.component

import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.bookingdetail.BookingDetailFragment
import com.future.pms.ui.history.HistoryFragment
import com.future.pms.ui.home.HomeFragment
import com.future.pms.ui.ongoing.OngoingFragment
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.ui.profile.ProfileFragment
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.ui.scan.ScanFragment
import com.future.pms.ui.superadmin.homesuperadmin.HomeFragmentSuperAdmin
import com.future.pms.ui.superadmin.listactivity.ListActivityFragment
import com.future.pms.ui.superadmin.listcustomer.ListCustomerFragment
import com.future.pms.ui.superadmin.listparkingzone.ListParkingZoneFragment
import dagger.Component

@Component(modules = [FragmentModule::class]) interface FragmentComponent {
  fun inject(homeFragment: HomeFragment)

  fun inject(scanFragment: ScanFragment)

  fun inject(profileFragment: ProfileFragment)

  fun inject(parkingDirectionFragment: ParkingDirectionFragment)

  fun inject(receiptFragment: ReceiptFragment)

  fun inject(historyFragment: HistoryFragment)

  fun inject(ongoingFragment: OngoingFragment)

  fun inject(bookingDetailFragment: BookingDetailFragment)

  fun inject(homeFragmentSuperAdmin: HomeFragmentSuperAdmin)

  fun inject(listActivityFragment: ListActivityFragment)

  fun inject(listCustomerFragment: ListCustomerFragment)

  fun inject(listParkingZoneFragment: ListParkingZoneFragment)
}