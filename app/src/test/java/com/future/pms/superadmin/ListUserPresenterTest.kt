package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.listuser.network.ListUserApi
import com.future.pms.superadmin.listuser.presenter.ListUserPresenter
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class ListUserPresenterTest : BaseTest() {
  @Mock lateinit var listUserApi: ListUserApi
  @InjectMocks lateinit var listUserPresenter: ListUserPresenter

  @Test fun loadAllCustomerSuccess() {
    `when`(listUserApi.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.just(customerResponse()))

    listUserPresenter.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)
  }

  @Test fun loadAllCustomerFailed() {
    `when`(listUserApi.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)
  }

  @Test fun loadAllAdminSuccess() {
    `when`(listUserApi.loadAllParkingZone(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.just(admin()))

    listUserPresenter.loadAllAdmin(ACCESS_TOKEN, PAGE, NAME)
  }

  @Test fun loadAllAdminFailed() {
    `when`(listUserApi.loadAllParkingZone(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllAdmin(ACCESS_TOKEN, PAGE, NAME)
  }

  @Test fun loadAllSuperAdminSuccess() {
    `when`(listUserApi.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.just(superAdmin()))

    listUserPresenter.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)
  }

  @Test fun loadAllSuperAdminFailed() {
    `when`(listUserApi.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)
  }
}