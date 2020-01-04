package com.future.pms.ui.superadmin.homesuperadmin

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.model.user.User
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import com.future.pms.util.Authentication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenterSuperAdmin @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: HomeContractSuperAdmin

  fun createUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = api.createUser(accessToken, user).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.createUserSuccess()
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun updateUser(accessToken: String, email: String, password: String, role: String) {
    val user = User(email, password, role)
    val subscribe = api.updateUser(accessToken, user).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.updateUserSuccess()
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  fun getEmail(accessToken: String) {
    val subscribe = api.getEmail(accessToken).subscribeOn(Schedulers.io()).observeOn(
        AndroidSchedulers.mainThread()).subscribe({
      view.getEmailSuccess(it)
    }, {
      view.onFailed(it.message.toString())
    })
    subscriptions.add(subscribe)
  }

  private fun getContext(): Context? {
    return when (view) {
      is Fragment -> (view as Fragment).context
      is Activity -> (view as Activity)
      else -> throw Exception()
    }
  }

  fun signOut() {
    getContext()?.let { Authentication.delete(it) }
  }

  fun attach(view: HomeContractSuperAdmin) {
    this.view = view
  }

  fun subscribe() {
    //No implement required
  }
}