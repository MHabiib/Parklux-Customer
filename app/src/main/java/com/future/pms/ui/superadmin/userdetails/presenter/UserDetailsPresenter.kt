package com.future.pms.ui.superadmin.userdetails.presenter

import com.future.pms.model.admin.ParkingZoneResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.register.CustomerRequest
import com.future.pms.model.user.User
import com.future.pms.model.user.UserDetails
import com.future.pms.ui.base.BasePresenter
import com.future.pms.ui.superadmin.userdetails.network.UserDetailsApi
import com.future.pms.ui.superadmin.userdetails.view.UserDetailsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor() : BasePresenter<UserDetailsContract>() {
  @Inject lateinit var userDetailsApi: UserDetailsApi

  fun loadDataCustomer(idCustomer: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = userDetailsApi.getCustomerDetailSA(idCustomer, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
          { customer: Customer ->
            showProgress(false)
            loadDataCustomerSuccess(customer)
          }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun loadDataAdmin(id: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = userDetailsApi.getAdminDetailSA(id, accessToken).subscribeOn(
          Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        loadDataAdminSuccess(it)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun loadDataSuperAdmin(id: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      val subscribe = userDetailsApi.getUserDetailSA(id, accessToken).subscribeOn(
          Schedulers.io()).observeOn(
          AndroidSchedulers.mainThread()).subscribe({ userDetails: UserDetails ->
        showProgress(false)
        loadDataSuperAdminSuccess(userDetails)
      }, { error ->
        showProgress(false)
        onFailed(error.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun getUpdatedCustomer(id: String, accessToken: String) {
    val subscribe = userDetailsApi.getCustomerDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.getUpdatedCustomerSuccess(it)
    }, { error ->
      view?.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getUpdatedAdmin(id: String, accessToken: String) {
    val subscribe = userDetailsApi.getAdminDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.getUpdatedAdminSuccess(it)
    }, { error ->
      view?.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getUpdatedSuperAdmin(id: String, accessToken: String) {
    val subscribe = userDetailsApi.getUserDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({ userDetails: UserDetails ->
      view?.getUpdatedSuperAdminSuccess(userDetails)
    }, { error ->
      view?.onFailed(error.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateCustomer(id: String, name: String, email: String, password: String, phoneNumber: String,
      token: String) {
    view?.apply {
      showProgress(true)
      val customer = CustomerRequest(email, name, password, phoneNumber)
      val subscribe = userDetailsApi.updateCustomer(id, token, customer).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateCustomerSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun updateAdmin(id: String, name: String, email: String, phoneNumber: String, price: String,
      openHour: String, address: String, password: String, token: String) {
    view?.apply {
      showProgress(true)
      val priceInDouble: Double = if (price == "") {
        0.0
      } else {
        price.toDouble()
      }
      val parkingZone = ParkingZoneResponse(address, email, name, openHour, password, phoneNumber,
          priceInDouble, "")
      val subscribe = userDetailsApi.updateAdmin(id, token, parkingZone).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateAdminSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun updateSuperAdmin(id: String, accessToken: String, email: String, password: String,
      role: String) {
    view?.apply {
      showProgress(true)
      val user = User(email, password, role)
      val subscribe = userDetailsApi.updateUserFromList(id, accessToken, user).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateSuperAdminSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      })
      subscriptions.add(subscribe)
    }
  }

  fun banCustomer(id: String, accessToken: String) {
    val subscribe = userDetailsApi.banCustomer(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.updateCustomerSuccess()
    }, {
      view?.onFailed(it.toString())
    })
    subscriptions.add(subscribe)
  }

  fun deleteSuperAdmin(id: String, accessToken: String) {
    val subscribe = userDetailsApi.deleteSuperAdmin(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view?.deleteSuperAdminSuccess(it)
    }, {
      view?.onFailed(it.toString())
    })
    subscriptions.add(subscribe)
  }
}