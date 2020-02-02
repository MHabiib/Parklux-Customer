package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.mainsuperadmin.presenter.MainPresenterSuperAdmin
import com.future.pms.superadmin.mainsuperadmin.view.MainContractSuperAdmin
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify

class MainPresenterSuperAdminTest : BaseTest() {
  @Mock lateinit var mainContract: MainContractSuperAdmin
  @InjectMocks lateinit var mainPresenter: MainPresenterSuperAdmin

  @Test fun onHomeIconClick() {
    mainPresenter.onHomeIconClick()

    verify(mainContract).showHomeFragment()
  }

  @Test fun attach() {
    mainPresenter.attach(mainContract)

    verify(mainContract).showHomeFragment()
  }

  @Test fun onActivityClick() {
    mainPresenter.onActivityClick()

    verify(mainContract).showListActivityFragment()
  }

  @Test fun onCustomerClick() {
    mainPresenter.onCustomerClick()

    verify(mainContract).showListCustomerFragment()
  }
}