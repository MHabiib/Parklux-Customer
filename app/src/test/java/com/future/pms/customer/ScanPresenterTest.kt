package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.scan.network.ScanApi
import com.future.pms.scan.presenter.ScanPresenter
import com.future.pms.scan.view.ScanContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ScanPresenterTest : BaseTest() {
  @Mock lateinit var scanApi: ScanApi
  @Mock lateinit var scanContract: ScanContract
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

  @Test fun scanContractNull() {
    scanContract = mock(null)
    scanPresenter.createBooking(ID, FCM_TOKEN, ACCESS_TOKEN)
  }
}