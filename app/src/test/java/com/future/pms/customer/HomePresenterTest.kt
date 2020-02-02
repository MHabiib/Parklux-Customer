package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.home.network.HomeApi
import com.future.pms.home.presenter.HomePresenter
import com.future.pms.home.view.HomeContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class HomePresenterTest : BaseTest() {
  @Mock lateinit var homeApi: HomeApi
  @Mock lateinit var homeContract: HomeContract
  @InjectMocks lateinit var homePresenter: HomePresenter

  @Test fun loadDataSuccess() {
    `when`(homeApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(Observable.just(customer()))

    homePresenter.loadData(ACCESS_TOKEN)

    verify(homeContract).loadCustomerDetailSuccess(customer())
  }

  @Test fun loadDataFailed() {
    `when`(homeApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    homePresenter.loadData(ACCESS_TOKEN)

    verify(homeContract).onFailed(ERROR)
  }

  @Test fun onOngoingIconClick() {
    homePresenter.onOngoingIconClick()

    verify(homeContract).showOngoingFragment()
  }

  @Test fun onHistoryIconClick() {
    homePresenter.onHistoryIconClick()

    verify(homeContract).showHistoryFragment()
  }
}