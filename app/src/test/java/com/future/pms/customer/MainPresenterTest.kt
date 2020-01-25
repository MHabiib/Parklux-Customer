package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.main.presenter.MainPresenter
import com.future.pms.main.view.MainContract
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify

class MainPresenterTest : BaseTest() {
  @Mock lateinit var mainContract: MainContract
  @InjectMocks lateinit var mainPresenter: MainPresenter

  @Test fun onHomeIconClick() {
    mainPresenter.onHomeIconClick()
    verify(mainContract).showHomeFragment()
  }

  @Test fun onScanIconClick() {
    mainPresenter.onScanIconClick()
    verify(mainContract).showScanFragment()
  }

  @Test fun onProfileIconClick() {
    mainPresenter.onProfileIconClick()
    verify(mainContract).showProfileFragment()
  }

  @Test fun showBookingDetail() {
    mainPresenter.showBookingDetail(ID_BOOKING)
    verify(mainContract).showBookingDetail(ID_BOOKING)
  }

  @Test fun showBookingFailed() {
    mainPresenter.showBookingFailed()
    verify(mainContract).showBookingFailed()
  }

  @Test fun showParkingDirection() {
    mainPresenter.showParkingDirection(ID_BOOKING, LEVEL_NAME)
    verify(mainContract).showParkingDirection(ID_BOOKING, LEVEL_NAME)
  }

  @Test fun attach() {
    mainPresenter.attach(mainContract)
    verify(mainContract).showHomeFragment()
  }
}