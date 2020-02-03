package com.future.pms.superadmin.userdetails.presenter

import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Customer
import com.future.pms.superadmin.listuser.model.admin.ParkingZoneResponse
import com.future.pms.superadmin.userdetails.model.User
import com.future.pms.superadmin.userdetails.model.UserDetails
import com.future.pms.superadmin.userdetails.network.UserDetailsApi
import com.future.pms.superadmin.userdetails.view.UserDetailsContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailsPresenter @Inject constructor(private val userDetailsApi: UserDetailsApi) :
    BasePresenter<UserDetailsContract>() {

  fun loadDataCustomer(idCustomer: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(userDetailsApi.getCustomerDetailSA(idCustomer, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
          { customer: com.future.pms.core.model.customerdetails.Customer ->
            showProgress(false)
            loadDataCustomerSuccess(customer)
          }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      }))
    }
  }

  fun loadDataAdmin(id: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(userDetailsApi.getAdminDetailSA(id, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        loadDataAdminSuccess(it)
      }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      }))
    }
  }

  fun loadDataSuperAdmin(id: String, accessToken: String) {
    view?.apply {
      showProgress(true)
      subscriptions.add(userDetailsApi.getUserDetailSA(id, accessToken).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
          { userDetails: UserDetails ->
            showProgress(false)
            loadDataSuperAdminSuccess(userDetails)
          }, { error ->
        showProgress(false)
        onFailed(error.message.toString())
      }))
    }
  }

  fun getUpdatedCustomer(id: String, accessToken: String) {
    subscriptions.add(userDetailsApi.getCustomerDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.getUpdatedCustomerSuccess(it)
    }, { error ->
      view?.onFailed(error.message.toString())
    }))
  }

  fun getUpdatedAdmin(id: String, accessToken: String) {
    subscriptions.add(userDetailsApi.getAdminDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.getUpdatedAdminSuccess(it)
    }, { error ->
      view?.onFailed(error.message.toString())
    }))
  }

  fun getUpdatedSuperAdmin(id: String, accessToken: String) {
    subscriptions.add(userDetailsApi.getUserDetailSA(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
        { userDetails: UserDetails ->
          view?.getUpdatedSuperAdminSuccess(userDetails)
        }, { error ->
      view?.onFailed(error.message.toString())
    }))
  }

  fun updateCustomer(id: String, name: String, email: String, password: String, phoneNumber: String,
      token: String) {
    view?.apply {
      showProgress(true)
      val customer = Customer(email, name, password, phoneNumber)
      subscriptions.add(userDetailsApi.updateCustomer(id, token, customer).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateCustomerSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      }))
    }
  }

  fun updateAdmin(id: String, token: String, parkingZone: ParkingZoneResponse) {
    view?.apply {
      showProgress(true)
      subscriptions.add(userDetailsApi.updateAdmin(id, token, parkingZone).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateAdminSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      }))
    }
  }

  fun updateSuperAdmin(id: String, accessToken: String, email: String, password: String,
      role: String) {
    view?.apply {
      showProgress(true)
      val user = User(email, password, role)
      subscriptions.add(userDetailsApi.updateUserFromList(id, accessToken, user).subscribeOn(
          Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
        showProgress(false)
        updateSuperAdminSuccess()
      }, {
        showProgress(false)
        onFailed(it.message.toString())
      }))
    }
  }

  fun banCustomer(id: String, accessToken: String) {
    subscriptions.add(userDetailsApi.banCustomer(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.updateCustomerSuccess()
    }, {
      view?.onFailed(it.toString())
    }))
  }

  fun deleteSuperAdmin(id: String, accessToken: String) {
    subscriptions.add(userDetailsApi.deleteSuperAdmin(id, accessToken).subscribeOn(
        Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
      view?.deleteSuperAdminSuccess(it)
    }, {
      view?.onFailed(it.toString())
    }))
  }
}