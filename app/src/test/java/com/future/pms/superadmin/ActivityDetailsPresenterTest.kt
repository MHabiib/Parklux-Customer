package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.activitydetails.network.ActivityDetailsApi
import com.future.pms.superadmin.activitydetails.presenter.ActivityDetailsPresenter
import com.future.pms.superadmin.activitydetails.view.ActivityDetailsContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ActivityDetailsPresenterTest : BaseTest() {
  @Mock lateinit var activityDetailsApi: ActivityDetailsApi
  @Mock lateinit var activityDetailsContract: ActivityDetailsContract
  @InjectMocks lateinit var activityDetailsPresenter: ActivityDetailsPresenter

  @Test fun bookingReceiptSASuccess() {
    `when`(activityDetailsApi.bookingReceiptSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(receipt()))

    activityDetailsPresenter.bookingReceiptSA(ID, ACCESS_TOKEN)

    verify(activityDetailsContract).showProgress(true)
    verify(activityDetailsContract).showProgress(false)
    verify(activityDetailsContract).bookingReceiptSASuccess(receipt())
  }

  @Test fun bookingReceiptSAFailed() {
    `when`(activityDetailsApi.bookingReceiptSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    activityDetailsPresenter.bookingReceiptSA(ID, ACCESS_TOKEN)

    verify(activityDetailsContract).showProgress(true)
    verify(activityDetailsContract).showProgress(false)
    verify(activityDetailsContract).onFailed("java.lang.Exception: error")
  }

  @Test fun checkoutBookingSASuccess() {
    `when`(activityDetailsApi.checkoutBookingSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(receipt()))

    activityDetailsPresenter.checkoutBookingSA(ID, ACCESS_TOKEN)

    verify(activityDetailsContract).showProgress(true)
    verify(activityDetailsContract).showProgress(false)
    verify(activityDetailsContract).checkoutBookingSASuccess(receipt())
  }

  @Test fun checkoutBookingSAFailed() {
    `when`(activityDetailsApi.checkoutBookingSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    activityDetailsPresenter.checkoutBookingSA(ID, ACCESS_TOKEN)


    verify(activityDetailsContract).showProgress(true)
    verify(activityDetailsContract).showProgress(false)
    verify(activityDetailsContract).onFailed("java.lang.Exception: error")
  }
}