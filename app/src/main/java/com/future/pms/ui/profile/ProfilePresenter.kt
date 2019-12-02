package com.future.pms.ui.profile

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.register.CustomerRequest
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: ProfileContract

  fun loadData(accessToken: String) {
    val subscribe = api.getCustomerDetail(accessToken).subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread()).subscribe({ customer: Customer ->
        view.loadCustomerDetailSuccess(customer)
      }, { error ->
        view.showErrorMessage(error.localizedMessage)
        view.unauthorized()
      })
    subscriptions.add(subscribe)
  }

  fun update(name: String, email: String, password: String, phoneNumber: String, token: String) {
    val customer = CustomerRequest(email, name, password, phoneNumber)
    val subscribe = api.putUpdateCustomer(token, customer).subscribeOn(Schedulers.io()).observeOn(
      AndroidSchedulers.mainThread()
    ).subscribe({
      view.showProgress(false)
      view.onSuccess()
    }, {
      view.showProgress(false)
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  private fun getContext(): Context {
    return when (view) {
      is Fragment -> (view as Fragment).context!!
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }

  fun signOut() {
    Authentication.delete(getContext())
  }

  fun attach(view: ProfileContract) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}