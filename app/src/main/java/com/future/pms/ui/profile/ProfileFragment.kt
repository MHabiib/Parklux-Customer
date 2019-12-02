package com.future.pms.ui.profile

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
import com.future.pms.R
import com.future.pms.databinding.FragmentProfileBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.ui.login.LoginActivity
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.PROFILE_FRAGMENT
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract {
  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var binding: FragmentProfileBinding
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
    injectDependency()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    val logout = binding.btnLogout
    update = binding.btnEditProfile
    logout.setOnClickListener {
      btnLogout.visibility = View.GONE
      presenter.signOut()
      onLogout()
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    presenter.attach(this)
    presenter.subscribe()
    presenter.loadData(accessToken)
    update?.setOnClickListener {
      showProgress(true)
      presenter.update(
        binding.profileName.text.toString(),
        binding.profileEmail.text.toString(),
        binding.profilePassword.text.toString(),
        binding.profilePhoneNumber.text.toString(),
        accessToken
      )
    }
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar && show) {
      progressBar.visibility = View.VISIBLE
    } else if (null != progressBar && !show) {
      progressBar.visibility = View.GONE
    }
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(Constants.ERROR).e(error)
  }

  override fun loadCustomerDetailSuccess(customer: Customer) {
    binding.profileNameDisplay.text = customer.body.name
    binding.profileName.setText(customer.body.name)
    binding.profileEmail.setText(customer.body.email)
    binding.profilePassword.hint = "********"
    if (customer.body.phoneNumber == "") {
      binding.profilePhoneNumber.hint = "You haven't enter your phone number yet !"
    } else {
      binding.profilePhoneNumber.setText(customer.body.phoneNumber)
    }
    binding.profileName.addTextChangedListener(textWatcher())
    binding.profileEmail.addTextChangedListener(textWatcher())
    binding.profilePassword.addTextChangedListener(textWatcher())
    binding.profilePhoneNumber.addTextChangedListener(textWatcher())
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

  override fun onFailed(e: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun unauthorized() {
    val activity = activity as MainActivity?
    activity?.presenter?.showLoginPage()
  }

  override fun onLogout() {
    val intent = Intent(activity, LoginActivity::class.java)
    startActivity(intent)
  }

  private fun refreshPage() {
    val ft = fragmentManager!!.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft.setReorderingAllowed(false)
    }
    ft.detach(this).attach(this).commit()
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
      FragmentModule()
    ).build()
    profileComponent.inject(this)
  }
}