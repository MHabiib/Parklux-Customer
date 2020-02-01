package com.future.pms.superadmin

import com.future.pms.base.BaseTest
import com.future.pms.superadmin.homesuperadmin.network.HomeApiSuperAdmin
import com.future.pms.superadmin.homesuperadmin.presenter.HomePresenterSuperAdmin
import com.future.pms.superadmin.homesuperadmin.view.HomeContractSuperAdmin
import com.future.pms.util.Constants
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class HomeSuperAdminPresenterTest : BaseTest() {
  @Mock lateinit var homeSuperAdminApi: HomeApiSuperAdmin
  @Mock lateinit var homeSuperAdminContract: HomeContractSuperAdmin
  @InjectMocks lateinit var homeSuperAdminPresenter: HomePresenterSuperAdmin

  @Test fun createUserSuccess() {
    `when`(homeSuperAdminApi.createUser(ACCESS_TOKEN, user())).thenReturn(Observable.just(SUCCESS))

    homeSuperAdminPresenter.createUser(ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun createUserFailed() {
    `when`(homeSuperAdminApi.createUser(ACCESS_TOKEN, user())).thenReturn(
        Observable.error(Exception(ERROR)))

    homeSuperAdminPresenter.createUser(ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun updateUserSuccess() {
    `when`(homeSuperAdminApi.updateUser(ACCESS_TOKEN, user())).thenReturn(Observable.just(SUCCESS))

    homeSuperAdminPresenter.updateUser(ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun updateUserFailed() {
    `when`(homeSuperAdminApi.updateUser(ACCESS_TOKEN, user())).thenReturn(
        Observable.error(Exception(ERROR)))

    homeSuperAdminPresenter.updateUser(ACCESS_TOKEN, EMAIL, PASSWORD, ROLE)
  }

  @Test fun getEmailSuccess() {
    `when`(homeSuperAdminApi.getEmail(ACCESS_TOKEN)).thenReturn(Observable.just(SUCCESS))

    homeSuperAdminPresenter.getEmail(ACCESS_TOKEN)
  }

  @Test fun getEmailFailed() {
    `when`(homeSuperAdminApi.getEmail(ACCESS_TOKEN)).thenReturn(Observable.error(Exception(ERROR)))

    homeSuperAdminPresenter.getEmail(ACCESS_TOKEN)
  }

  @Test fun refreshTokenSuccess() {
    `when`(homeSuperAdminApi.refresh(Constants.GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.just(token()))

    homeSuperAdminPresenter.refreshToken(REFRESH_TOKEN)

    Mockito.verify(homeSuperAdminContract).onSuccess(token())
  }

  @Test fun refreshTokenFailed() {
    `when`(homeSuperAdminApi.refresh(Constants.GRANT_TYPE_REFRESH, REFRESH_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    homeSuperAdminPresenter.refreshToken(REFRESH_TOKEN)

    Mockito.verify(homeSuperAdminContract).onLogout()
  }
}