package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.maps.network.MapsApi
import com.future.pms.maps.presenter.MapsPresenter
import com.future.pms.maps.view.MapsContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class MapsPresenterTest : BaseTest() {
  @Mock lateinit var mapsApi: MapsApi
  @Mock lateinit var mapsContract: MapsContract
  @InjectMocks lateinit var ongoinPresenter: MapsPresenter

  @Test fun loadLocationSuccess() {
    Mockito.`when`(mapsApi.getLatLng(ACCESS_TOKEN)).thenReturn(
        Observable.just(listParkingZoneLatLng()))

    ongoinPresenter.loadLocation(ACCESS_TOKEN)

    Mockito.verify(mapsContract).loadLocationSuccess(listParkingZoneLatLng())
  }

  @Test fun loadLocationFailed() {
    Mockito.`when`(mapsApi.getLatLng(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    ongoinPresenter.loadLocation(ACCESS_TOKEN)

    Mockito.verify(mapsContract).onFailed(ERROR)
  }
}