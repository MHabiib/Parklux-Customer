package com.future.pms.customer

import com.future.pms.base.BaseTest
import com.future.pms.profile.network.ProfileApi
import com.future.pms.profile.presenter.ProfilePresenter
import com.future.pms.profile.view.ProfileContract
import io.reactivex.Observable
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class ProfilePresenterTest : BaseTest() {
  @Mock lateinit var profileApi: ProfileApi
  @Mock lateinit var profileContract: ProfileContract
  @InjectMocks lateinit var profilePresenter: ProfilePresenter

  @Test fun loadDataSuccess() {
    `when`(profileApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(Observable.just(customer()))

    profilePresenter.loadData(ACCESS_TOKEN)

    verify(profileContract).loadCustomerDetailSuccess(customer())
  }

  @Test fun loadDataFailed() {
    `when`(profileApi.getCustomerDetail(ACCESS_TOKEN)).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.loadData(ACCESS_TOKEN)

    verify(profileContract).onFailed(ERROR)
  }

  @Test fun updateSuccess() {
    `when`(profileApi.putUpdateCustomer(ACCESS_TOKEN, customerWithPassword())).thenReturn(
        Observable.just(customerWithPassword()))

    profilePresenter.update(NAME, EMAIL, PASSWORD, PHONE_NUMBER, ACCESS_TOKEN)

    verify(profileContract).showProgress(false)
    verify(profileContract).onSuccess()
  }

  @Test fun updateFailed() {
    `when`(profileApi.putUpdateCustomer(ACCESS_TOKEN, customerWithPassword())).thenReturn(
        Observable.error(Exception(ERROR)))

    profilePresenter.update(NAME, EMAIL, PASSWORD, PHONE_NUMBER, ACCESS_TOKEN)

    verify(profileContract).showProgress(false)
    verify(profileContract).onFailed(ERROR)
  }
}