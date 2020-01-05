package com.future.pms.ui.superadmin.homesuperadmin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
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
import com.future.pms.util.Constants.Companion.NO_CONNECTION
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class HomeFragmentSuperAdmin : Fragment(), HomeContractSuperAdmin {
  @Inject lateinit var presenter: HomePresenterSuperAdmin
  private lateinit var binding: FragmentHomeSuperAdminBinding
  private lateinit var accessToken: String

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
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
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
    presenter.signOut()
    presenter.getEmail(accessToken)
  }

  override fun getEmailSuccess(email: String) = binding.txtEmail.setText(email)

  override fun onFailed(e: String) {
    if (e.contains(NO_CONNECTION)) {
      Toast.makeText(context, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
    }
    Timber.e(e)
  }

  private fun hideKeyboard() = activity?.window?.setSoftInputMode(
      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

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