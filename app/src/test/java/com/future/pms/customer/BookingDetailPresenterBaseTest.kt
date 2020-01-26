package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.bookingdetail.network.BookingDetailApi
import com.future.pms.bookingdetail.presenter.BookingDetailPresenter
import com.future.pms.bookingdetail.view.BookingDetailContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class BookingDetailPresenterBaseTest : BaseTest() {
  @Mock lateinit var bookingDetailApi: BookingDetailApi
  @Mock lateinit var bookingDetailContract: BookingDetailContract
  @InjectMocks lateinit var bookingDetailPresenter: BookingDetailPresenter

  @Test fun loadBookingSuccess() {
    `when`(bookingDetailApi.getOngoingBooking(ACCESS_TOKEN)).thenReturn(
        Observable.just(customerBooking()))

    bookingDetailPresenter.loadBooking(ACCESS_TOKEN)

    verify(bookingDetailContract).showProgress(false)
    verify(bookingDetailContract).showProgress(true)
    verify(bookingDetailContract).loadBookingSuccess(customerBooking())
  }

  @Test fun loadBookingFailed() {
    `when`(bookingDetailApi.getOngoingBooking(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    bookingDetailPresenter.loadBooking(ACCESS_TOKEN)

    verify(bookingDetailContract).showProgress(false)
    verify(bookingDetailContract).showProgress(true)
    verify(bookingDetailContract).showErrorMessage(ERROR)
  }

  @Test fun getParkingLayoutSuccess() {
    `when`(bookingDetailApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(PARKING_LAYOUT))

    bookingDetailPresenter.getParkingLayout(ID, ACCESS_TOKEN)

    verify(bookingDetailContract).showProgress(false)
    verify(bookingDetailContract).showProgress(true)
    verify(bookingDetailContract).getLayoutSuccess(PARKING_LAYOUT)
  }

  @Test fun getParkingLayoutFailed() {
    `when`(bookingDetailApi.getParkingLayout(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    bookingDetailPresenter.getParkingLayout(ID, ACCESS_TOKEN)

    verify(bookingDetailContract).showProgress(false)
    verify(bookingDetailContract).showProgress(true)
    verify(bookingDetailContract).showErrorMessage(ERROR)
  }
}