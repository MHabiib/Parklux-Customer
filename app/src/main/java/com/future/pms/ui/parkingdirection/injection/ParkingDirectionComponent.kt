package com.future.pms.ui.parkingdirection.injection

import com.future.pms.ui.base.BaseComponent
import com.future.pms.ui.parkingdirection.view.ParkingDirectionFragment
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [ParkingDirectionModule::class])
interface ParkingDirectionComponent {
  fun inject(parkingDirectionFragment: ParkingDirectionFragment)
}