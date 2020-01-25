package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.parkingdirection.network.ParkingDirectionApi
import com.future.pms.parkingdirection.presenter.ParkingDirectionPresenter
import com.future.pms.parkingdirection.view.ParkingDirectionContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ParkingDirectionPresenterTest : BaseTest() {
  @Mock lateinit var parkingDirectionApi: ParkingDirectionApi
  @Mock lateinit var parkingDirectionContract: ParkingDirectionContract
  @InjectMocks lateinit var ongoinPresenter: ParkingDirectionPresenter

  @Test fun getParkingLayoutSuccess() {
    `when`(parkingDirectionApi.getParkingLayout(ID_BOOKING, ACCESS_TOKEN)).thenReturn(
        Observable.just(LAYOUT))

    ongoinPresenter.getParkingLayout(ID_BOOKING, ACCESS_TOKEN)

    verify(parkingDirectionContract).getLayoutSuccess(LAYOUT)
  }

  @Test fun getParkingLayoutFailed() {
    `when`(parkingDirectionApi.getParkingLayout(ID_BOOKING, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    ongoinPresenter.getParkingLayout(ID_BOOKING, ACCESS_TOKEN)

    verify(parkingDirectionContract).showProgress(false)
    verify(parkingDirectionContract).onFailed(ERROR)
  }
}