package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.userdetails.network.UserDetailsApi
import com.future.pms.superadmin.userdetails.presenter.UserDetailsPresenter
import com.future.pms.superadmin.userdetails.view.UserDetailsContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class UserDetailsPresenterTest : BaseTest() {
  @Mock lateinit var userDetailsApi: UserDetailsApi
  @Mock lateinit var userDetailsContract: UserDetailsContract
  @InjectMocks lateinit var userDetailsPresenter: UserDetailsPresenter

  @Test fun loadDataCustomerSuccess() {
    `when`(userDetailsApi.getCustomerDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(customerUserDetails()))

    userDetailsPresenter.loadDataCustomer(ID, ACCESS_TOKEN)

    verify(userDetailsContract).loadDataCustomerSuccess(customerUserDetails())
  }

  @Test fun loadDataCustomerFailed() {
    `when`(userDetailsApi.getCustomerDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.loadDataCustomer(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun loadDataAdminSuccess() {
    `when`(userDetailsApi.getAdminDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(adminUserDetails()))

    userDetailsPresenter.loadDataAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).loadDataAdminSuccess(adminUserDetails())
  }

  @Test fun loadDataAdminFailed() {
    `when`(userDetailsApi.getAdminDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.loadDataAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun loadDataSuperAdminSuccess() {
    `when`(userDetailsApi.getUserDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(userDetails()))

    userDetailsPresenter.loadDataSuperAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).loadDataSuperAdminSuccess(userDetails())
  }

  @Test fun loadDataSuperAdminFailed() {
    `when`(userDetailsApi.getUserDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.loadDataSuperAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun getUpdatedCustomerSuccess() {
    `when`(userDetailsApi.getCustomerDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(customerUserDetails()))

    userDetailsPresenter.getUpdatedCustomer(ID, ACCESS_TOKEN)

    verify(userDetailsContract).getUpdatedCustomerSuccess(customerUserDetails())
  }

  @Test fun getUpdatedCustomerFailed() {
    `when`(userDetailsApi.getCustomerDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.getUpdatedCustomer(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun getUpdatedAdminSuccess() {
    `when`(userDetailsApi.getAdminDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(adminUserDetails()))

    userDetailsPresenter.getUpdatedAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).getUpdatedAdminSuccess(adminUserDetails())
  }

  @Test fun getUpdatedAdminFailed() {
    `when`(userDetailsApi.getAdminDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.getUpdatedAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun getUpdatedSuperAdminSuccess() {
    `when`(userDetailsApi.getUserDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.just(userDetails()))

    userDetailsPresenter.getUpdatedSuperAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).getUpdatedSuperAdminSuccess(userDetails())
  }

  @Test fun getUpdatedSuperAdminFailed() {
    `when`(userDetailsApi.getUserDetailSA(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.getUpdatedSuperAdmin(ID, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun updateCustomerSuccess() {
    `when`(userDetailsApi.updateCustomer(ID, ACCESS_TOKEN, customerWithPassword())).thenReturn(
        Observable.just(customerWithPassword()))

    userDetailsPresenter.updateCustomer(ID, NAME, EMAIL, PASSWORD, PHONE_NUMBER, ACCESS_TOKEN)

    verify(userDetailsContract).updateCustomerSuccess()
  }

  @Test fun updateCustomerFailed() {
    `when`(userDetailsApi.updateCustomer(ID, ACCESS_TOKEN, customerWithPassword())).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.updateCustomer(ID, NAME, EMAIL, PASSWORD, PHONE_NUMBER, ACCESS_TOKEN)

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun updateAdminSuccess() {
    `when`(userDetailsApi.updateAdmin(ID, ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.just(parkingZone()))

    userDetailsPresenter.updateAdmin(ID, ACCESS_TOKEN, parkingZone())

    verify(userDetailsContract).updateAdminSuccess()
  }

  @Test fun updateAdminFailed() {
    `when`(userDetailsApi.updateAdmin(ID, ACCESS_TOKEN, parkingZone())).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.updateAdmin(ID, ACCESS_TOKEN, parkingZone())

    verify(userDetailsContract).onFailed(ERROR)
  }

  @Test fun updateSuperAdminSuccess() {
    `when`(userDetailsApi.updateUserFromList(ID, ACCESS_TOKEN, user())).thenReturn(
        Observable.just(STR))

    userDetailsPresenter.updateSuperAdmin(ID, ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun updateSuperAdminFailed() {
    `when`(userDetailsApi.updateUserFromList(ID, ACCESS_TOKEN, user())).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.updateSuperAdmin(ID, ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun banCustomerSuccess() {
    `when`(userDetailsApi.banCustomer(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    userDetailsPresenter.banCustomer(ID, ACCESS_TOKEN)
  }

  @Test fun banCustomerFailed() {
    `when`(userDetailsApi.banCustomer(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.banCustomer(ID, ACCESS_TOKEN)
  }

  @Test fun deleteSuperAdminSuccess() {
    `when`(userDetailsApi.deleteSuperAdmin(ID, ACCESS_TOKEN)).thenReturn(Observable.just(STR))

    userDetailsPresenter.deleteSuperAdmin(ID, ACCESS_TOKEN)
  }

  @Test fun deleteSuperAdminFailed() {
    `when`(userDetailsApi.deleteSuperAdmin(ID, ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    userDetailsPresenter.deleteSuperAdmin(ID, ACCESS_TOKEN)
  }
}