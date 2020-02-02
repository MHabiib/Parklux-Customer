package com.future.pms.parkingdirection.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.parkingdirection.view.ParkingDirectionFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ParkingDirectionModule::class])
interface ParkingDirectionComponent {
  fun inject(parkingDirectionFragment: ParkingDirectionFragment)
}