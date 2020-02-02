package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.loginsuperadmin.network.LoginSuperAdminApi
import com.future.pms.superadmin.loginsuperadmin.presenter.LoginPresenterSuperAdmin
import com.future.pms.superadmin.loginsuperadmin.view.LoginContractSuperAdmin
import com.future.pms.util.Constants
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

class LoginSuperAdminPresenterTest : BaseTest() {
  @Mock lateinit var loginApi: LoginSuperAdminApi
  @Mock lateinit var loginContract: LoginContractSuperAdmin
  @InjectMocks lateinit var loginPresenter: LoginPresenterSuperAdmin

  @Test fun loginSuccess() {
    Mockito.`when`(loginApi.auth(USERNAME, PASSWORD, Constants.GRANT_TYPE)).thenReturn(
        Observable.just(token()))

    loginPresenter.login(USERNAME, PASSWORD)

    Mockito.verify(loginContract).onSuccess(token())
  }

  @Test fun loginFailed() {
    Mockito.`when`(loginApi.auth(USERNAME, PASSWORD, Constants.GRANT_TYPE)).thenReturn(
        Observable.error(Exception(ERROR)))

    loginPresenter.login(USERNAME, PASSWORD)

    Mockito.verify(loginContract).onFailed(ERROR)
  }

  @Test fun loadDataSuccess() {
    Mockito.`when`(loginApi.isSuperAdmin(ACCESS_TOKEN)).thenReturn(Observable.just(""))

    loginPresenter.isSuperAdmin(ACCESS_TOKEN)

    Mockito.verify(loginContract).onAuthorized()
  }

  @Test fun loadDataFailed() {
    Mockito.`when`(loginApi.isSuperAdmin(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    loginPresenter.isSuperAdmin(ACCESS_TOKEN)

    Mockito.verify(loginContract).onFailed(ERROR)
  }
}