package com.future.pms.ui.superadmin.listparkingzone

import com.future.pms.di.base.BaseMVPView
import com.future.pms.model.parkingzone.ParkingZone

interface ListParkingZoneContract : BaseMVPView {
  fun onFailed(e: String)
  fun loadAllParkingZoneSuccess(parkingZone: ParkingZone)
}