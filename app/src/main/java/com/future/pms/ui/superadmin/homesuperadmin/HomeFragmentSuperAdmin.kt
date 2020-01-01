package com.future.pms.ui.superadmin.homesuperadmin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.databinding.FragmentHomeSuperAdminBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.ui.superadmin.loginsuperadmin.LoginActivitySuperAdmin
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT_SUPER_ADMIN
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class HomeFragmentSuperAdmin : Fragment(), HomeContractSuperAdmin {
  @Inject lateinit var presenter: HomePresenterSuperAdmin
  private lateinit var binding: FragmentHomeSuperAdminBinding

  companion object {
    const val TAG: String = HOME_FRAGMENT_SUPER_ADMIN
  }

  fun newInstance(): HomeFragmentSuperAdmin {
    return HomeFragmentSuperAdmin()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_super_admin, container,
        false)
    presenter.attach(this)
    with(binding) {
      val logout = btnLogout
      logout.setOnClickListener {
        btnLogout.visibility = View.GONE
        presenter.signOut()
        onLogout()
      }
      btnAddCustomer.setOnClickListener {
        if (btnAddCustomer.text == getString(R.string.plus)) {
          inputLayoutCustomer.visibility = View.VISIBLE
          btnSaveCustomer.visibility = View.VISIBLE
          btnAddCustomer.text = getString(R.string.minus)
        } else {
          inputLayoutCustomer.visibility = View.GONE
          btnSaveCustomer.visibility = View.GONE
          btnAddCustomer.text = getString(R.string.plus)
          hideKeyboard()
        }
      }
      btnAddAdmin.setOnClickListener {
        if (btnAddAdmin.text == getString(R.string.plus)) {
          inputLayoutAdmin.visibility = View.VISIBLE
          btnSaveAdmin.visibility = View.VISIBLE
          btnAddAdmin.text = getString(R.string.minus)
        } else {
          inputLayoutAdmin.visibility = View.GONE
          btnSaveAdmin.visibility = View.GONE
          btnAddAdmin.text = getString(R.string.plus)
          hideKeyboard()
        }
      }
      btnAddSuperAdmin.setOnClickListener {
        if (btnAddSuperAdmin.text == getString(R.string.plus)) {
          inputLayoutSuperAdmin.visibility = View.VISIBLE
          btnSaveSuperAdmin.visibility = View.VISIBLE
          btnAddSuperAdmin.text = getString(R.string.minus)
        } else {
          inputLayoutSuperAdmin.visibility = View.GONE
          btnSaveSuperAdmin.visibility = View.GONE
          btnAddSuperAdmin.text = getString(R.string.plus)
          hideKeyboard()
        }
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      subscribe()
    }
  }

  override fun onFailed(e: String) {
    Timber.e(e)
  }

  private fun hideKeyboard() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
  }

  override fun onLogout() {
    val intent = Intent(activity, LoginActivitySuperAdmin::class.java)
    startActivity(intent)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}