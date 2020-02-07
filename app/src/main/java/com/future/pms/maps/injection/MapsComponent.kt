package com.future.pms.maps.injection

import com.future.pms.core.base.BaseComponent
import com.future.pms.maps.view.MapsActivity
import dagger.Component

@Component(dependencies = [BaseComponent::class], modules = [MapsModule::class])
interface MapsComponent {
  fun inject(mapsActivity: MapsActivity)
}