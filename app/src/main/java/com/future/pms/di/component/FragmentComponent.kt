package com.future.pms.di.component

import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.home.HomeFragment
import com.future.pms.ui.parkingdirection.ParkingDirectionFragment
import com.future.pms.ui.profile.ProfileFragment
import com.future.pms.ui.scan.ScanFragment
import dagger.Component

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(homeFragment: HomeFragment)

    fun inject(scanFragment: ScanFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject (parkingDirectionFragment: ParkingDirectionFragment)
}