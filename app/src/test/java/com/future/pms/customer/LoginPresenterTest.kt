package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.login.network.LoginApi
import com.future.pms.login.presenter.LoginPresenter
import com.future.pms.login.view.LoginContract
import com.future.pms.util.Constants.Companion.GRANT_TYPE
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class LoginPresenterTest : BaseTest() {
  @Mock lateinit var loginApi: LoginApi
  @Mock lateinit var loginContract: LoginContract
  @InjectMocks lateinit var loginPresenter: LoginPresenter

  @Test fun loginSuccess() {
    `when`(loginApi.auth(USERNAME, PASSWORD, GRANT_TYPE)).thenReturn(Observable.just(token()))

    loginPresenter.login(USERNAME, PASSWORD)

    verify(loginContract).onSuccess(token())
  }

  @Test fun loginFailed() {
    `when`(loginApi.auth(USERNAME, PASSWORD, GRANT_TYPE)).thenReturn(
        Observable.error(Exception(ERROR)))

    loginPresenter.login(USERNAME, PASSWORD)

    verify(loginContract).onFailed(ERROR)
  }

  @Test fun loadDataSuccess() {
    `when`(loginApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(Observable.just(customer()))

    loginPresenter.loadData(ACCESS_TOKEN)

    verify(loginContract).onAuthorized()
  }

  @Test fun loadDataFailed() {
    `when`(loginApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    loginPresenter.loadData(ACCESS_TOKEN)

    verify(loginContract).onFailed(ERROR)
  }
}