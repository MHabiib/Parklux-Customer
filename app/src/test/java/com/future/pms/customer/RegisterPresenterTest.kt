package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.register.network.RegisterApi
import com.future.pms.register.presenter.RegisterPresenter
import com.future.pms.register.view.RegisterContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`

class RegisterPresenterTest : BaseTest() {
  @Mock lateinit var registerApi: RegisterApi
  @Mock lateinit var registerContract: RegisterContract
  @InjectMocks lateinit var registerPresenter: RegisterPresenter

  @Test fun registerSuccess() {
    `when`(registerApi.postCreateCustomer(customerWithPassword())).thenReturn(
        Observable.just(USERNAME))

    registerPresenter.register(NAME, EMAIL, PASSWORD, PHONE_NUMBER)
  }

  @Test fun registerFailed() {
    `when`(registerApi.postCreateCustomer(customerWithPassword())).thenReturn(
        Observable.error(Exception(ERROR)))

    registerPresenter.register(NAME, EMAIL, PASSWORD, PHONE_NUMBER)
  }
}