package com.future.pms.ui.superadmin.userdetails

import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.user.UserResponse
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: UserDetailsContract

  fun loadDataCustomer(idCustomer: String, accessToken: String) {
    val subscribe = api.getCustomerDetailSA(idCustomer, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ customer: Customer ->
      view.loadDataCustomerSuccess(customer)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun loadDataAdmin(id: String, accessToken: String) {
    val subscribe = api.getAdminDetailSA(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.loadDataAdminSuccess(it)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun loadDataSuperAdmin(id: String, accessToken: String) {
    val subscribe = api.getUserDetailSA(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ userResponse: UserResponse ->
      view.loadDataSuperAdminSuccess(userResponse)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: UserDetailsContract) {
    this.view = view
  }
}