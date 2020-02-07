package com.future.pms.superadmin.homesuperadmin.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
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
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentHomeSuperAdminBinding
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = HOME_FRAGMENT_SUPER_ADMIN
  }

  fun newInstance(): HomeFragmentSuperAdmin = HomeFragmentSuperAdmin()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_super_admin, container,
        false)
    presenter.attach(this)
    with(binding) {
      val logout = btnLogout
      logout.setOnClickListener {
        btnLogout.visibility = View.GONE
        context?.let { context -> Authentication.delete(context) }
        onLogout()
      }

      var isOpen = ""
      btnAddAdmin.setOnClickListener {
        hideKeyboard()
        if (btnAddAdmin.text == getString(R.string.plus)) {
          if (isOpen == ROLE_SUPER_ADMIN) {
            inputLayoutSuperAdmin.visibility = View.GONE
            btnSaveSuperAdmin.visibility = View.GONE
            btnAddSuperAdmin.text = getString(R.string.plus)
            ivSuperAdmin.visibility = View.VISIBLE
          }
          inputLayoutAdmin.visibility = View.VISIBLE
          btnSaveAdmin.visibility = View.VISIBLE
          btnAddAdmin.text = getString(R.string.hide)
          ivAdmin.visibility = View.GONE
          isOpen = ROLE_ADMIN
        } else {
          inputLayoutAdmin.visibility = View.GONE
          ivAdmin.visibility = View.VISIBLE
          btnSaveAdmin.visibility = View.GONE
          btnAddAdmin.text = getString(R.string.plus)
          isOpen = ""
        }
      }
      btnAddSuperAdmin.setOnClickListener {
        hideKeyboard()
        if (btnAddSuperAdmin.text == getString(R.string.plus)) {
          if (isOpen == ROLE_ADMIN) {
            inputLayoutAdmin.visibility = View.GONE
            btnSaveAdmin.visibility = View.GONE
            btnAddAdmin.text = getString(R.string.plus)
            ivAdmin.visibility = View.VISIBLE
          }
          inputLayoutSuperAdmin.visibility = View.VISIBLE
          btnSaveSuperAdmin.visibility = View.VISIBLE
          btnAddSuperAdmin.text = getString(R.string.hide)
          ivSuperAdmin.visibility = View.GONE
          isOpen = ROLE_SUPER_ADMIN
        } else {
          inputLayoutSuperAdmin.visibility = View.GONE
          ivSuperAdmin.visibility = View.VISIBLE
          btnSaveSuperAdmin.visibility = View.GONE
          btnAddSuperAdmin.text = getString(R.string.plus)
          isOpen = ""
        }
      }
      btnSaveAdmin.setOnClickListener {
        hideKeyboard()
        if (isValid(ROLE_ADMIN)) {
          showProgress(true)
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
          showProgress(true)
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
        if (txtPassword.isEnabled) {
          txtPassword.isEnabled = false
          txtPassword.setText("")
          btnSaveAccount.visibility = View.GONE
          btnUpdateAccount.text = getString(R.string.update)
          hideKeyboard()
        } else {
          txtPassword.isEnabled = true
          txtPassword.requestFocus()
          btnUpdateAccount.text = getString(R.string.cancel)
          showKeyboard()
          btnSaveAccount.visibility = View.VISIBLE
        }
      }
      btnSaveAccount.setOnClickListener {
        if (isValid(Constants.UPDATE_SUPER_ADMIN)) {
          txtPassword.isEnabled = false
          btnSaveAccount.visibility = View.GONE
          hideKeyboard()
          showProgress(true)
          presenter.updateUser(accessToken, txtEmail.text.toString(), txtPassword.text.toString(),
              ROLE_SUPER_ADMIN)
          txtPassword.text?.clear()
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      showProgress(true)
      getEmail(accessToken)
      subscribe()
    }
  }

  override fun createUserSuccess() {
    showProgress(false)
    Toast.makeText(context, getString(R.string.success), Toast.LENGTH_LONG).show()
  }

  private fun isValid(role: String): Boolean {
    when (role) {
      ROLE_ADMIN -> {
        if (!binding.inputEmailAdmin.text.toString().isEmailValid()) return false
        if (binding.inputPasswordAdmin.text.toString().isEmpty()) return false
        return true
      }
      ROLE_SUPER_ADMIN -> {
        if (!binding.inputEmailSuperAdmin.text.toString().isEmailValid()) return false
        if (binding.inputPasswordSuperAdmin.text.toString().isEmpty()) return false
        return true
      }
      else -> {
        if (!binding.txtEmail.text.toString().isEmailValid()) return false
        if (binding.txtPassword.text.toString().isEmpty()) return false
        return true
      }
    }
  }

  private fun String.isEmailValid(): Boolean = !TextUtils.isEmpty(
      this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

  override fun updateUserSuccess() {
    showProgress(false)
    Toast.makeText(context, getString(R.string.update_account_success), Toast.LENGTH_LONG).show()
  }

  override fun getEmailSuccess(email: String) {
    showProgress(false)
    binding.txtEmail.setText(email)
  }

  override fun onFailed(message: String) {
    showProgress(false)
    when {
      message.contains(NO_CONNECTION) -> {
        Toast.makeText(context, getString(R.string.no_network_connection),
            Toast.LENGTH_SHORT).show()
        binding.ibRefresh.visibility = View.VISIBLE
        binding.ibRefresh.setOnClickListener {
          showProgress(true)
          presenter.getEmail(accessToken)
        }
      }
      message.contains(Constants.BAD_REQUEST_CODE) -> Toast.makeText(context,
          getString(R.string.email_already_registered), Toast.LENGTH_SHORT).show()
      message.contains(Constants.UNAUTHORIZED_CODE) -> {
        presenter.refreshToken(accessToken)
      }
    }
    Timber.tag(Constants.ERROR).e(message)
    Timber.e(message)
  }

  private fun hideKeyboard() {
    val view = activity?.currentFocus
    view?.let {
      val mInputMethodManager = activity?.getSystemService(
          Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      mInputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  override fun onSuccess(token: Token) {
    context?.let {
      Authentication.save(it, token, gson.fromJson(
          it.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
              Constants.TOKEN, null), Token::class.java).role)
    }
    accessToken = token.accessToken
    showProgress(true)
    presenter.getEmail(accessToken)
  }

  private fun showKeyboard() {
    val view = activity?.currentFocus
    view?.let {
      val mInputMethodManager = activity?.getSystemService(
          Activity.INPUT_METHOD_SERVICE) as InputMethodManager
      mInputMethodManager.toggleSoftInput(InputMethod.SHOW_FORCED, 0)
    }
  }

  private fun showProgress(show: Boolean) {
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

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