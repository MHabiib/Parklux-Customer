package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.listactivity.network.ListActivityApi
import com.future.pms.superadmin.listactivity.presenter.ListActivityPresenter
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ListActivityPresenterTest : BaseTest() {
  @Mock lateinit var listActivityApi: ListActivityApi
  @InjectMocks lateinit var listActivityPresenter: ListActivityPresenter

  @Test fun loadAllBookingSuccess() {
    `when`(listActivityApi.loadAllBooking(ACCESS_TOKEN, PAGE, FILTER)).thenReturn(
        Observable.just(booking()))

    listActivityPresenter.loadAllBooking(ACCESS_TOKEN, PAGE, FILTER)
  }

  @Test fun loadAllBookingFailed() {
    `when`(listActivityApi.loadAllBooking(ACCESS_TOKEN, PAGE, FILTER)).thenReturn(
        Observable.error(Exception(ERROR)))

    listActivityPresenter.loadAllBooking(ACCESS_TOKEN, PAGE, FILTER)
  }

  @Test fun findBookingByIdSuccess() {
    `when`(listActivityApi.findBookingById(ID, ACCESS_TOKEN)).thenReturn(Observable.just(content()))

    listActivityPresenter.findBookingById(ID, ACCESS_TOKEN)
  }

  @Test fun findBookingByIdFailed() {
    `when`(listActivityApi.findBookingById(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    listActivityPresenter.findBookingById(ID, ACCESS_TOKEN)
  }
}