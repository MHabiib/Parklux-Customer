package com.future.pms.ui.superadmin.homesuperadmin

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.future.pms.network.ApiServiceInterface
import com.future.pms.network.RetrofitClient
import com.future.pms.util.Authentication
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomePresenterSuperAdmin @Inject constructor() {
  private val subscriptions = CompositeDisposable()
  private val api: ApiServiceInterface = RetrofitClient.create()
  private lateinit var view: HomeContractSuperAdmin

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