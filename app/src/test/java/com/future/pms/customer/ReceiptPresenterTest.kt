package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.receipt.network.ReceiptApi
import com.future.pms.receipt.presenter.ReceiptPresenter
import com.future.pms.receipt.view.ReceiptContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ReceiptPresenterTest : BaseTest() {
  @Mock lateinit var receiptApi: ReceiptApi
  @Mock lateinit var receiptContract: ReceiptContract
  @InjectMocks lateinit var receiptPresenter: ReceiptPresenter

  @Test fun loadDataSuccess() {
    `when`(receiptApi.getBookingReceipt(ID, ACCESS_TOKEN)).thenReturn(Observable.just(receipt()))

    receiptPresenter.loadData(ACCESS_TOKEN, ID)
  }

  @Test fun loadDataFailed() {
    `when`(receiptApi.getBookingReceipt(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    receiptPresenter.loadData(ACCESS_TOKEN, ID)
  }
}