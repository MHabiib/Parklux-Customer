package com.future.pms.ui.profile.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.databinding.FragmentProfileBinding
import com.future.pms.model.customerdetail.Body
import com.future.pms.model.oauth.Token
import com.future.pms.ui.login.view.LoginActivity
import com.future.pms.ui.main.view.MainActivity
import com.future.pms.ui.profile.injection.DaggerProfileComponent
import com.future.pms.ui.profile.injection.ProfileComponent
import com.future.pms.ui.profile.presenter.ProfilePresenter
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
  private var update: Button? = null

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
      update = btnEditProfile
      logout.setOnClickListener {
        btnLogout.visibility = View.GONE
        presenter.signOut()
        onLogout()
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(context?.getSharedPreferences(Constants.AUTHENTICATION,
            Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.attach(this)
    presenter.apply {
      showProgress(true)
      subscribe()
      loadData(accessToken)
      update?.setOnClickListener {
        showProgress(true)
        update(binding.profileName.text.toString(), binding.profileEmail.text.toString(),
            binding.profilePassword.text.toString(), binding.profilePhoneNumber.text.toString(),
            accessToken)
      }
    }
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
      if (customer.phoneNumber == "") {
        profilePhoneNumber.hint = "You haven't enter your phone number yet !"
      } else {
        profilePhoneNumber.setText(customer.phoneNumber)
      }
      profileName.addTextChangedListener(textWatcher())
      profileEmail.addTextChangedListener(textWatcher())
      profilePassword.addTextChangedListener(textWatcher())
      profilePhoneNumber.addTextChangedListener(textWatcher())
    }
    showProgress(false)
  }

  private fun textWatcher(): TextWatcher {
    return object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        binding.btnEditProfile.setBackgroundResource(R.drawable.card_layout_purple)
        binding.btnEditProfile.isEnabled = true
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
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
        presenter.signOut()
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