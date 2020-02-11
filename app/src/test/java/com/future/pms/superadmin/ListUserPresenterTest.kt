package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.listuser.network.ListUserApi
import com.future.pms.superadmin.listuser.presenter.ListUserPresenter
import com.future.pms.superadmin.listuser.view.ListUserContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ListUserPresenterTest : BaseTest() {
  @Mock lateinit var listUserApi: ListUserApi
  @Mock lateinit var listUserContract: ListUserContract
  @InjectMocks lateinit var listUserPresenter: ListUserPresenter

  @Test fun loadAllCustomerSuccess() {
    `when`(listUserApi.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.just(customerResponse()))

    listUserPresenter.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)

    verify(listUserContract).loadAllCustomerSuccess(customerResponse())
  }

  @Test fun loadAllCustomerFailed() {
    `when`(listUserApi.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllCustomer(ACCESS_TOKEN, PAGE, NAME)

    verify(listUserContract).onFailed(ERROR)
  }

  @Test fun loadAllAdminSuccess() {
    `when`(listUserApi.loadAllParkingZone(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.just(admin()))

    listUserPresenter.loadAllAdmin(ACCESS_TOKEN, PAGE, NAME)

    verify(listUserContract).loadAllAdminSuccess(admin())
  }

  @Test fun loadAllAdminFailed() {
    `when`(listUserApi.loadAllParkingZone(ACCESS_TOKEN, PAGE, NAME)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllAdmin(ACCESS_TOKEN, PAGE, NAME)

    verify(listUserContract).onFailed(ERROR)
  }

  @Test fun loadAllSuperAdminSuccess() {
    `when`(listUserApi.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.just(superAdmin()))

    listUserPresenter.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)

    verify(listUserContract).loadAllSuperAdminSuccess(superAdmin())
  }

  @Test fun loadAllSuperAdminFailed() {
    `when`(listUserApi.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)).thenReturn(
        Observable.error(Exception(ERROR)))

    listUserPresenter.loadAllSuperAdmin(ACCESS_TOKEN, PAGE)

    verify(listUserContract).onFailed(ERROR)
  }
}