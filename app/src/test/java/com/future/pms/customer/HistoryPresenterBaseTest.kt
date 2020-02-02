package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.history.network.HistoryApi
import com.future.pms.history.presenter.HistoryPresenter
import com.future.pms.history.view.HistoryContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class HistoryPresenterBaseTest : BaseTest() {
  @Mock lateinit var historyApi: HistoryApi
  @Mock lateinit var historyContract: HistoryContract
  @InjectMocks lateinit var historyPresenter: HistoryPresenter

  @Test fun loadCustomerBookingSuccess() {
    `when`(historyApi.getCustomerBooking(ACCESS_TOKEN, 0)).thenReturn(Observable.just(history()))

    historyPresenter.loadCustomerBooking(ACCESS_TOKEN, 0)

    verify(historyContract).loadCustomerBookingSuccess(history())
  }

  @Test fun loadCustomerBookingFailed() {
    `when`(historyApi.getCustomerBooking(ACCESS_TOKEN, 0)).thenReturn(
        Observable.error(Exception(ERROR)))

    historyPresenter.loadCustomerBooking(ACCESS_TOKEN, 0)

    verify(historyContract).onFailed(ERROR)
  }


}