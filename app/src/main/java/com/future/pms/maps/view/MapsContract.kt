package com.future.pms.maps.view

import com.future.pms.core.base.BaseView
import com.future.pms.maps.model.ParkingZoneLatLng

interface MapsContract : BaseView {
  fun loadLocationSuccess(list: List<ParkingZoneLatLng>)
}