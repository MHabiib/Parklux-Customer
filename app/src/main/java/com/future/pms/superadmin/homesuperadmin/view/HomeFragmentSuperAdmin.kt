package com.future.pms.superadmin.homesuperadmin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.Token
import com.future.pms.core.network.Authentication
import com.future.pms.databinding.FragmentHomeSuperAdminBinding
import com.future.pms.superadmin.homesuperadmin.injection.DaggerHomeComponentSuperAdmin
import com.future.pms.superadmin.homesuperadmin.injection.HomeComponentSuperAdmin
import com.future.pms.superadmin.homesuperadmin.presenter.HomePresenterSuperAdmin
import com.future.pms.superadmin.loginsuperadmin.view.LoginActivitySuperAdmin
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT_SUPER_ADMIN
import com.future.pms.util.Constants.Companion.NO_CONNECTION
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class HomeFragmentSuperAdmin : BaseFragment(), HomeContractSuperAdmin {
  private var daggerBuild: HomeComponentSuperAdmin = DaggerHomeComponentSuperAdmin.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: HomePresenterSuperAdmin
  private lateinit var binding: FragmentHomeSuperAdminBinding
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = HOME_FRAGMENT_SUPER_ADMIN
  }

  fun newInstance(): HomeFragmentSuperAdmin {
    return HomeFragmentSuperAdmin()
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
        context?.let { it1 -> Authentication.delete(it1) }
        onLogout()
      }

      var isOpen = ""
      btnAddAdmin.setOnClickListener {
        if (btnAddAdmin.text == getString(R.string.plus)) {
          if (isOpen == ROLE_SUPER_ADMIN) {
            inputLayoutSuperAdmin.visibility = View.GONE
            btnSaveSuperAdmin.visibility = View.GONE
            btnAddSuperAdmin.text = getString(R.string.plus)
          }
          inputLayoutAdmin.visibility = View.VISIBLE
          btnSaveAdmin.visibility = View.VISIBLE
          btnAddAdmin.text = getString(R.string.minus)
          isOpen = ROLE_ADMIN
        } else {
          inputLayoutAdmin.visibility = View.GONE
          btnSaveAdmin.visibility = View.GONE
          btnAddAdmin.text = getString(R.string.plus)
          hideKeyboard()
          isOpen = ""
        }
      }
      btnAddSuperAdmin.setOnClickListener {
        if (btnAddSuperAdmin.text == getString(R.string.plus)) {
          if (isOpen == ROLE_ADMIN) {
            inputLayoutAdmin.visibility = View.GONE
            btnSaveAdmin.visibility = View.GONE
            btnAddAdmin.text = getString(R.string.plus)
          }
          inputLayoutSuperAdmin.visibility = View.VISIBLE
          btnSaveSuperAdmin.visibility = View.VISIBLE
          btnAddSuperAdmin.text = getString(R.string.minus)
          isOpen = ROLE_SUPER_ADMIN
        } else {
          inputLayoutSuperAdmin.visibility = View.GONE
          btnSaveSuperAdmin.visibility = View.GONE
          btnAddSuperAdmin.text = getString(R.string.plus)
          hideKeyboard()
          isOpen = ""
        }
      }
      btnSaveAdmin.setOnClickListener {
        hideKeyboard()
        if (isValid(ROLE_ADMIN)) {
          presenter.createUser(accessToken, inputEmailAdmin.text.toString(),
              inputPasswordAdmin.text.toString(), ROLE_ADMIN)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
        inputEmailAdmin.text?.clear()
        inputPasswordAdmin.text?.clear()
      }
      btnSaveSuperAdmin.setOnClickListener {
        hideKeyboard()
        if (isValid(ROLE_SUPER_ADMIN)) {
          presenter.createUser(accessToken, inputEmailSuperAdmin.text.toString(),
              inputPasswordSuperAdmin.text.toString(), ROLE_SUPER_ADMIN)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
        inputEmailSuperAdmin.text?.clear()
        inputPasswordSuperAdmin.text?.clear()
      }
      btnUpdateAccount.setOnClickListener {
        hideKeyboard()
        if (isValid(Constants.UPDATE_SUPER_ADMIN)) {
          presenter.updateUser(accessToken, txtEmail.text.toString(), txtPassword.text.toString(),
              ROLE_SUPER_ADMIN)
          txtPassword.text?.clear()
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      getEmail(accessToken)
      subscribe()
    }
  }

  override fun createUserSuccess() = Toast.makeText(context, getString(R.string.success),
      Toast.LENGTH_LONG).show()

  private fun isValid(role: String): Boolean {
    when (role) {
      ROLE_ADMIN -> {
        if (binding.inputEmailAdmin.toString().isEmpty()) return false
        if (binding.inputPasswordAdmin.toString().isEmpty()) return false
        return true
      }
      ROLE_SUPER_ADMIN -> {
        if (binding.inputEmailSuperAdmin.toString().isEmpty()) return false
        if (binding.inputPasswordSuperAdmin.toString().isEmpty()) return false
        return true
      }
      else -> {
        if (binding.txtEmail.toString().isEmpty()) return false
        if (binding.txtPassword.toString().isEmpty()) return false
        return true
      }
    }
  }

  override fun updateUserSuccess() {
    Toast.makeText(context, getString(R.string.update_account_success), Toast.LENGTH_LONG).show()
    presenter.getEmail(accessToken)
  }

  override fun getEmailSuccess(email: String) = binding.txtEmail.setText(email)

  override fun onFailed(message: String) {
    when {
      message.contains(NO_CONNECTION) -> Toast.makeText(context,
          getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      message.contains(Constants.BAD_REQUEST_CODE) -> Toast.makeText(context,
          "Failed to update profile, email already used !", Toast.LENGTH_SHORT).show()
      message.contains(Constants.UNAUTHORIZED_CODE) -> {
        context?.let { Authentication.delete(it) }
        onLogout()
      }
    }
    Timber.tag(Constants.ERROR).e(message)
    binding.ibRefresh.visibility = View.VISIBLE
    binding.ibRefresh.setOnClickListener {
      presenter.getEmail(accessToken)
    }
    Timber.e(message)
  }

  private fun hideKeyboard() = activity?.window?.setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

  override fun onLogout() {
    val intent = Intent(activity, LoginActivitySuperAdmin::class.java)
    startActivity(intent)
    activity?.finish()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}