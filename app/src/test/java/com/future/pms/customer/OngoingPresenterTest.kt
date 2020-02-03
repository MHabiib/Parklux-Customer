package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.ongoing.network.OngoingApi
import com.future.pms.ongoing.presenter.OngoingPresenter
import com.future.pms.ongoing.view.OngoingContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class OngoingPresenterTest : BaseTest() {
  @Mock lateinit var ongoingApi: OngoingApi
  @Mock lateinit var ongoingContract: OngoingContract
  @InjectMocks lateinit var ongoinPresenter: OngoingPresenter

  @Test fun loadOngoingBookingSuccess() {
    `when`(ongoingApi.getOngoingBooking(ACCESS_TOKEN)).thenReturn(
        Observable.just(customerBooking()))

    ongoinPresenter.loadOngoingBooking(ACCESS_TOKEN)

    verify(ongoingContract).showProgress(false)
    verify(ongoingContract).loadCustomerOngoingSuccess(customerBooking())
  }

  @Test fun loadOngoingBookingFailed() {
    `when`(ongoingApi.getOngoingBooking(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    ongoinPresenter.loadOngoingBooking(ACCESS_TOKEN)

    verify(ongoingContract).showProgress(false)
    verify(ongoingContract).loadCustomerOngoingFailed(ERROR)
    verify(ongoingContract).onFailed(ERROR)
  }

  @Test fun checkoutBookingSuccess() {
    `when`(ongoingApi.postBookingCheckout(ACCESS_TOKEN)).thenReturn(
        Observable.just(customerBooking()))

    ongoinPresenter.checkoutBooking(ACCESS_TOKEN)

    verify(ongoingContract).showProgress(false)
    verify(ongoingContract).checkoutSuccess(customerBooking().idBooking)
    verify(ongoingContract).refreshHome()
  }

  @Test fun checkoutBookingFailed() {
    `when`(ongoingApi.postBookingCheckout(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    ongoinPresenter.checkoutBooking(ACCESS_TOKEN)

    verify(ongoingContract).showProgress(false)
    verify(ongoingContract).onFailed(ERROR)
    verify(ongoingContract).refreshHome()
  }
}