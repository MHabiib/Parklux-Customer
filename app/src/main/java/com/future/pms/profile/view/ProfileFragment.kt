package com.future.pms.profile.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.model.Token
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.core.network.Authentication
import com.future.pms.databinding.FragmentProfileBinding
import com.future.pms.login.view.LoginActivity
import com.future.pms.main.view.MainActivity
import com.future.pms.profile.injection.DaggerProfileComponent
import com.future.pms.profile.injection.ProfileComponent
import com.future.pms.profile.presenter.ProfilePresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.BAD_REQUEST_CODE
import com.future.pms.util.Constants.Companion.NOT_FOUND_CODE
import com.future.pms.util.Constants.Companion.NO_CONNECTION
import com.future.pms.util.Constants.Companion.PROFILE_FRAGMENT
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract {
  private var daggerBuild: ProfileComponent = DaggerProfileComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var binding: FragmentProfileBinding
  private lateinit var accessToken: String
  private var editMode = false

  companion object {
    const val TAG: String = PROFILE_FRAGMENT
  }

  fun newInstance(): ProfileFragment {
    return ProfileFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    with(binding) {
      val logout = btnLogout
      logout.setOnClickListener {
        btnLogout.visibility = View.GONE
        context?.let { context -> Authentication.delete(context) }
        onLogout()
      }

      var profileNameTxt = ""
      var profileEmailTxt = ""
      var profilePasswordTxt = ""
      var profilePhoneNumberTxt = ""
      btnEditProfile.setOnClickListener {
        if (!editMode) {
          editMode = true
          btnSaveProfile.visibility = View.VISIBLE
          btnEditProfile.setTextColor(resources.getColor(R.color.red))
          btnEditProfile.text = getString(R.string.cancel)
          profileNameTxt = profileName.text.toString()
          profileEmailTxt = profileEmail.text.toString()
          profilePasswordTxt = profilePassword.text.toString()
          profilePhoneNumberTxt = profilePhoneNumber.text.toString()
          profileName.isEnabled = true
          profileName.isCursorVisible = true
          profileName.requestFocus()
          profileEmail.isEnabled = true
          profilePassword.isEnabled = true
          profilePhoneNumber.isEnabled = true
        } else {
          profileName.setText(profileNameTxt)
          profileEmail.setText(profileEmailTxt)
          profilePassword.setText(profilePasswordTxt)
          profilePhoneNumber.setText(profilePhoneNumberTxt)
          exitEditMode()
        }
      }
      btnSaveProfile.setOnClickListener {
        if (isValid()) {
          exitEditMode()
          showProgress(true)
          presenter.update(profileName.text.toString(), profileEmail.text.toString(),
              profilePassword.text.toString(), profilePhoneNumber.text.toString(), accessToken)
        } else {
          Toast.makeText(context, "Please fill all the entries with valid input",
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
    presenter.attach(this)
    presenter.apply {
      showProgress(true)
      subscribe()
      loadData(accessToken)
    }
  }

  private fun FragmentProfileBinding.exitEditMode() {
    profileName.isEnabled = false
    profileEmail.isEnabled = false
    profilePassword.isEnabled = false
    profilePhoneNumber.isEnabled = false
    editMode = false
    btnSaveProfile.visibility = View.GONE
    btnEditProfile.setTextColor(resources.getColor(R.color.colorAccent))
    btnEditProfile.text = getString(R.string.edit_profile)
  }

  private fun isValid(): Boolean {
    if (binding.profileName?.text.toString().isEmpty()) return false
    if (!binding.profileEmail?.text.toString().isEmailValid()) return false
    if (binding.profilePhoneNumber?.text.toString().isEmpty()) return false
    return true
  }

  private fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar) {
      if (show) {
        progressBar.visibility = View.VISIBLE
      } else {
        progressBar.visibility = View.GONE
      }
    }
  }

  override fun loadCustomerDetailSuccess(customer: Body) {
    with(binding) {
      ibRefresh.visibility = View.GONE
      profileNameDisplay.text = customer.name
      profileName.setText(customer.name)
      profileEmail.setText(customer.email)
      profilePassword.hint = "********"
      profilePhoneNumber.setText(customer.phoneNumber)
    }
    showProgress(false)
  }

  override fun onSuccess() {
    Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show()
    profile_password.text?.clear()
    refreshPage()
  }

  override fun onFailed(message: String) {
    when {
      message.contains(NO_CONNECTION) -> Toast.makeText(context,
          getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      message.contains(BAD_REQUEST_CODE) -> Toast.makeText(context,
          "Failed to update profile, email already used !", Toast.LENGTH_SHORT).show()
      message.contains(NOT_FOUND_CODE) -> {
        context?.let { Authentication.delete(it) }
        onLogout()
      }
    }
    Timber.tag(Constants.ERROR).e(message)
    binding.ibRefresh.visibility = View.VISIBLE
    binding.ibRefresh.setOnClickListener {
      showProgress(true)
      presenter.loadData(accessToken)
    }
    showProgress(false)
    Timber.e(message)
  }

  override fun onLogout() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
    activity?.finish()
  }

  private fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}