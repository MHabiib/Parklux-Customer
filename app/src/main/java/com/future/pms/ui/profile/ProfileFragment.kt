package com.future.pms.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.future.pms.R
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
import kotlinx.android.synthetic.main.fragment_profile.view.*
import javax.inject.Inject

class ProfileFragment : Fragment(), ProfileContract {
  @Inject lateinit var presenter: ProfilePresenter
  private lateinit var rootView: View
  private var update: Button? = null

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
    rootView = inflater.inflate(R.layout.fragment_profile, container, false)
    val submit = rootView.findViewById(R.id.btnLogout) as Button
    update = rootView.findViewById(R.id.btn_edit_profile) as Button
    submit.setOnClickListener {
      btnLogout.visibility = View.GONE
      presenter.signOut()
      onLogout()
    }
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).access_token
    presenter.attach(this)
    presenter.subscribe()
    presenter.loadData(accessToken)
    update?.setOnClickListener {
      presenter.update(
        profile_name.text.toString(),
        profile_email.text.toString(),
        profile_password.text.toString(),
        profile_phone_number.text.toString(),
        accessToken
      )
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

  override fun showErrorMessage(error: String) {
    Log.e(Constants.ERROR, error)
  }

  override fun loadCustomerDetailSuccess(customer: Customer) {
    rootView.profile_name_display.text = customer.body.name
    rootView.profile_name.setText(customer.body.name)
    rootView.profile_email.setText(customer.body.email)
    rootView.profile_password.hint = "********"
    if (customer.body.phoneNumber == "") {
      rootView.profile_phone_number.hint = "You haven't enter your phone number yet !"
    } else {
      rootView.profile_phone_number.setText(customer.body.phoneNumber)
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

  override fun onError(e: Throwable) {
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
    if (Build.VERSION.SDK_INT >= 26) {
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

  companion object {
    const val TAG: String = PROFILE_FRAGMENT
  }
}