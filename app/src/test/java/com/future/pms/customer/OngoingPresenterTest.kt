package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.ongoing.network.OngoingApi
import com.future.pms.ongoing.presenter.OngoingPresenter
import com.future.pms.ongoing.view.OngoingContract
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
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
    `when`(ongoingApi.postBookingCheckout(FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.just("".toResponseBody("".toMediaTypeOrNull())))

    ongoinPresenter.checkoutBooking(ACCESS_TOKEN, FCM_TOKEN)

    verify(ongoingContract).checkoutSuccess("".toResponseBody().string())
  }

  @Test fun checkoutBookingFailed() {
    `when`(ongoingApi.postBookingCheckout(FCM_TOKEN, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    ongoinPresenter.checkoutBooking(ACCESS_TOKEN, FCM_TOKEN)

    verify(ongoingContract).onFailed(ERROR)
  }
}