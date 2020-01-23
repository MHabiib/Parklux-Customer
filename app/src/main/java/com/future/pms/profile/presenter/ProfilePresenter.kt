package com.future.pms.profile.presenter

import android.content.Context
import com.future.pms.core.base.BasePresenter
import com.future.pms.core.model.Customer
import com.future.pms.core.model.Token
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.core.network.Authentication
import com.future.pms.profile.network.ProfileApi
import com.future.pms.profile.view.ProfileContract
import com.future.pms.util.Constants
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : BasePresenter<ProfileContract>() {
  @Inject lateinit var profileApi: ProfileApi

  fun loadData(accessToken: String) {
    subscriptions.add(
        profileApi.getCustomerDetail(accessToken).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe({ customer: Body ->
          view?.loadCustomerDetailSuccess(customer)
        }, {
          if (it.message.toString().contains(Constants.UNAUTHORIZED_CODE)) {
            getContext()?.let { Authentication.getRefresh(it) }?.let {
              profileApi.refresh(Constants.GRANT_TYPE_REFRESH, it).subscribeOn(
                  Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                  { token: Token ->
                    getContext()?.let { context ->
                      Authentication.save(context, token, Gson().fromJson(
                          context.getSharedPreferences(Constants.AUTHENTICATION,
                              Context.MODE_PRIVATE)?.getString(Constants.TOKEN, null),
                          Token::class.java).role)
                    }
                    loadData(accessToken)
                  }, { throwable ->
                view?.onFailed(throwable.message.toString())
              })
            }?.let {
              subscriptions.add(it)
            }
          } else {
            view?.onFailed(it.message.toString())
          }
        }))
  }

  fun update(name: String, email: String, password: String, phoneNumber: String, token: String) {
    view?.apply {
      val customer = Customer(email, name, password, phoneNumber)
      subscriptions.add(
          profileApi.putUpdateCustomer(token, customer).subscribeOn(Schedulers.io()).observeOn(
              AndroidSchedulers.mainThread()).subscribe({
            showProgress(false)
            onSuccess()
          }, {
            showProgress(false)
            onFailed(it.message.toString())
          }))
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }
}