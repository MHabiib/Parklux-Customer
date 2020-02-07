package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.scan.network.ScanApi
import com.future.pms.scan.presenter.ScanPresenter
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ScanPresenterTest : BaseTest() {
  @Mock lateinit var scanApi: ScanApi
  @InjectMocks lateinit var scanPresenter: ScanPresenter

  @Test fun createBookingSuccess() {
    `when`(scanApi.postCreateBooking(ID, FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.just(customerBooking()))

    scanPresenter.createBooking(ID, FCM_TOKEN, ACCESS_TOKEN)
  }

  @Test fun createBookingFailed() {
    `when`(scanApi.postCreateBooking(ID, FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    scanPresenter.createBooking(ID, FCM_TOKEN, ACCESS_TOKEN)
  }
}