package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.splash.network.SplashApi
import com.future.pms.splash.presenter.SplashPresenter
import com.future.pms.splash.view.SplashContract
import com.future.pms.util.Constants.Companion.GRANT_TYPE_REFRESH
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class SplashPresenterTest : BaseTest() {
  @Mock lateinit var splashApi: SplashApi
  @Mock lateinit var splashContract: SplashContract
  @InjectMocks lateinit var splashPresenter: SplashPresenter

  @Test fun refreshTokenSuccess() {
    `when`(splashApi.refresh(GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.just(token()))

    splashPresenter.refreshToken(REFRESH_TOKEN)

    verify(splashContract).onSuccess(token())
  }

  @Test fun refreshTokenFailed() {
    `when`(splashApi.refresh(GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    splashPresenter.refreshToken(REFRESH_TOKEN)

    verify(splashContract).onLogin()
  }
}