package com.future.pms.ui.superadmin.userdetails

import com.future.pms.model.admin.ParkingZoneResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.register.CustomerRequest
import com.future.pms.model.user.User
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

  fun getUpdatedCustomer(id: String, accessToken: String) {
    val subscribe = api.getCustomerDetailSA(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.getUpdatedCustomerSuccess(it)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getUpdatedAdmin(id: String, accessToken: String) {
    val subscribe = api.getAdminDetailSA(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.getUpdatedAdminSuccess(it)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getUpdatedSuperAdmin(id: String, accessToken: String) {
    val subscribe = api.getUserDetailSA(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ userResponse: UserResponse ->
      view.getUpdatedSuperAdminSuccess(userResponse)
    }, { error ->
      view.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateCustomer(id: String, name: String, email: String, password: String, phoneNumber: String,
      token: String) {
    val customer = CustomerRequest(email, name, password, phoneNumber)
    val subscribe = api.updateCustomer(id, token, customer).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.updateCustomerSuccess()
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateAdmin(id: String, name: String, email: String, phoneNumber: String, price: String,
      openHour: String, address: String, password: String, token: String) {
    val priceInDouble: Double = if (price == "") {
      0.0
    } else {
      price.toDouble()
    }
    val parkingZone = ParkingZoneResponse(address, email, name, openHour, password, phoneNumber,
        priceInDouble, "")
    val subscribe = api.updateAdmin(id, token, parkingZone).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.updateAdminSuccess()
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateSuperAdmin(id: String, accessToken: String, email: String, password: String,
      role: String) {
    val user = User(email, password, role)
    val subscribe = api.updateUserFromList(id, accessToken, user).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view.updateSuperAdminSuccess()
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun banCustomer(id: String, accessToken: String) {
    val subscribe = api.banCustomer(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.updateCustomerSuccess()
    }, {
      view.onFailed(it.toString())
    })
    subscriptions.add(subscribe)
  }

  fun deleteSuperAdmin(id: String, accessToken: String) {
    val subscribe = api.deleteSuperAdmin(id, accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.deleteSuperAdminSuccess(it)
    }, {
      view.onFailed(it.toString())
    })
    subscriptions.add(subscribe)
  }

  fun attach(view: UserDetailsContract) {
    this.view = view
  }
}