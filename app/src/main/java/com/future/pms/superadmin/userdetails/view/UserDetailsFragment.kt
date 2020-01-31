package com.future.pms.superadmin.userdetails.view

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.model.Token
import com.future.pms.core.model.customerdetails.Customer
import com.future.pms.databinding.FragmentBottomSheetUserDetailsBinding
import com.future.pms.superadmin.listuser.model.admin.ParkingZoneResponse
import com.future.pms.superadmin.listuser.model.admin.nonPage.AdminResponse
import com.future.pms.superadmin.listuser.view.ListUserFragment
import com.future.pms.superadmin.userdetails.injection.DaggerUserDetailsComponent
import com.future.pms.superadmin.userdetails.injection.UserDetailsComponent
import com.future.pms.superadmin.userdetails.model.UserDetails
import com.future.pms.superadmin.userdetails.presenter.UserDetailsPresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ID_USER
import com.future.pms.util.Constants.Companion.LIST_USER_FRAGMENT
import com.future.pms.util.Constants.Companion.ROLE
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import com.future.pms.util.Constants.Companion.UPDATE_ADMIN
import com.future.pms.util.Constants.Companion.UPDATE_CUSTOMER
import com.future.pms.util.Constants.Companion.UPDATE_SUPER_ADMIN
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UserDetailsFragment : BottomSheetDialogFragment(), UserDetailsContract {
  private var daggerBuild: UserDetailsComponent = DaggerUserDetailsComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: UserDetailsPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentBottomSheetUserDetailsBinding
  private lateinit var accessToken: String
  private lateinit var id: String

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_user_details,
        container, false)
    presenter.attach(this)
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    with(binding) {
      btnEditProfileCustomer.setOnClickListener {
        if (isValid(UPDATE_CUSTOMER)) {
          presenter.updateCustomer(this@UserDetailsFragment.id, profileNameCustomer.text.toString(),
              profileEmailCustomer.text.toString(), profilePasswordCustomer.text.toString(),
              profilePhoneNumberCustomer.text.toString(), accessToken)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }
      btnBanProfileCustomer.setOnClickListener {
        presenter.banCustomer(this@UserDetailsFragment.id, accessToken)
      }

      openHourAdmin.setOnClickListener {
        context?.let { context ->
          getDate(openHourAdmin, context)
        }
      }
      openHour2Admin.setOnClickListener {
        context?.let { context ->
          getDate(openHour2Admin, context)
        }
      }
      btnSaveAdmin.setOnClickListener {
        if (isValid(UPDATE_ADMIN)) {
          val price = binding.priceAdmin.text.toString()
          val priceInDouble: Double = if (price == "") {
            0.0
          } else {
            price.toDouble()
          }

          val parkingZone = ParkingZoneResponse(binding.addressAdmin.text.toString(),
              binding.profileEmailAdmin.text.toString(), binding.profileNameAdmin.text.toString(),
              String.format(getString(R.string.range2), binding.openHourAdmin.text.toString(),
                  binding.openHour2Admin.text.toString()), binding.passwordAdmin.text.toString(),
              binding.profilePhoneNumberAdmin.text.toString(), priceInDouble, "")
          presenter.updateAdmin(this@UserDetailsFragment.id, accessToken, parkingZone)
        } else {
          Toast.makeText(context, getString(R.string.fill_all_the_entries),
              Toast.LENGTH_LONG).show()
        }
      }

      btnDeleteProfileSuperAdmin.setOnClickListener {
        presenter.deleteSuperAdmin(this@UserDetailsFragment.id, accessToken)
      }
      btnEditProfileSuperAdmin.setOnClickListener {
        if (isValid(UPDATE_SUPER_ADMIN)) {
          presenter.updateSuperAdmin(this@UserDetailsFragment.id, accessToken,
              emailSuperAdmin.text.toString(), passwordSuperAdmin.text.toString(), ROLE_SUPER_ADMIN)
          passwordSuperAdmin.text?.clear()
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

    id = this.arguments?.getString(ID_USER).toString()
    when (this.arguments?.getString(ROLE).toString()) {
      ROLE_CUSTOMER -> {
        binding.layoutCustomer.visibility = View.VISIBLE
        presenter.loadDataCustomer(id, accessToken)
      }
      ROLE_ADMIN -> {
        binding.svLayoutAdmin.visibility = View.VISIBLE
        presenter.loadDataAdmin(id, accessToken)
      }
      ROLE_SUPER_ADMIN -> {
        binding.layoutSuperAdmin.visibility = View.VISIBLE
        presenter.loadDataSuperAdmin(id, accessToken)
      }
    }
  }

  override fun loadDataCustomerSuccess(customer: Customer) {
    with(binding) {
      tvName.text = customer.body.name
      id.text = customer.body.idCustomer
      profileNameCustomer.setText(customer.body.name)
      profileEmailCustomer.setText(customer.body.email)
      profilePasswordCustomer.hint = getString(R.string.password_hint)
      profilePhoneNumberCustomer.setText(customer.body.phoneNumber)
      if (customer.body.name.contains(" (BANNED)")) {
        btnBanProfileCustomer.text = getString(R.string.permit)
        btnBanProfileCustomer.setTextColor(resources.getColor(R.color.colorAccent))
        profileNameCustomer.isEnabled = false
      }
    }
  }

  override fun loadDataAdminSuccess(adminResponse: AdminResponse?) {
    with(binding) {
      adminResponse?.body?.let {
        tvName.text = it.name
        id.text = it.idParkingZone
        profileNameAdmin.setText(it.name)
        profileEmailAdmin.setText(it.emailAdmin)
        profilePhoneNumberAdmin.setText(it.phoneNumber)
        priceAdmin.text = null
        priceAdmin.hint = (String.format(getString(R.string.idr_price),
            Utils.thousandSeparator(it.price.toInt())))
        openHourAdmin.text = it.openHour.substring(0, 5)
        openHour2Admin.text = it.openHour.substring(8, 13)
        addressAdmin.setText(it.address)
        passwordAdmin.hint = getString(R.string.password_hint)
      }
    }
  }

  override fun loadDataSuperAdminSuccess(userDetails: UserDetails) {
    with(binding) {
      tvName.text = userDetails.email
      id.text = userDetails.idUser
      emailSuperAdmin.setText(userDetails.email)
      passwordSuperAdmin.hint = getString(R.string.password_hint)
    }
  }

  @SuppressLint("SimpleDateFormat") private fun getDate(textView: TextView, context: Context) {
    val cal = Calendar.getInstance()
    val dateSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      cal.set(Calendar.HOUR_OF_DAY, hour)
      cal.set(Calendar.MINUTE, minute)
      textView.text = SimpleDateFormat(getString(R.string.pattern)).format(cal.time)
    }

    textView.setOnClickListener {
      TimePickerDialog(context, dateSetListener, cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), true).show()
    }
  }

  override fun updateCustomerSuccess() = presenter.getUpdatedCustomer(id, accessToken)

  override fun updateAdminSuccess() = presenter.getUpdatedAdmin(id, accessToken)

  override fun updateSuperAdminSuccess() = presenter.getUpdatedSuperAdmin(id, accessToken)

  override fun getUpdatedCustomerSuccess(customer: Customer) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    customer.let { listUserFragment.updateCustomerSuccess(it) }
  }

  override fun getUpdatedAdminSuccess(adminResponse: AdminResponse?) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    adminResponse?.let { listUserFragment.updateAdminSuccess(it) }
  }

  override fun getUpdatedSuperAdminSuccess(userDetails: UserDetails) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    listUserFragment.updateSuperAdminSuccess(userDetails)
  }

  override fun deleteSuperAdminSuccess(response: String?) {
    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    listUserFragment.deleteSuperAdminSuccess()
  }

  private fun isValid(role: String): Boolean {
    with(binding) {
      when (role) {
        UPDATE_ADMIN -> {
          if (profileNameAdmin?.text.toString().isEmpty()) return false
          if (!profileEmailAdmin?.text.toString().isEmailValid()) return false
          if (profilePhoneNumberAdmin?.text.toString().isEmpty()) return false
          if (openHourAdmin?.text.toString().isEmpty()) return false
          if (openHour2Admin?.text.toString().isEmpty()) return false
          if (addressAdmin?.text.toString().isEmpty()) return false
        return true
      }
        UPDATE_SUPER_ADMIN -> {
          if (!emailSuperAdmin.text.toString().isEmailValid()) return false
          if (passwordSuperAdmin.text.toString().isEmpty()) return false
          return true
        }
        else -> {
          if (profileNameCustomer.text.toString().isEmpty()) return false
          if (!profileEmailCustomer.text.toString().isEmailValid()) return false
          if (profilePhoneNumberCustomer.text.toString().isEmpty()) return false
          return true
        }
      }
    }

  }

  private fun String.isEmailValid(): Boolean = !TextUtils.isEmpty(
      this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

  override fun onFailed(e: String) {
    context?.let {
      if (e.contains(Constants.NO_CONNECTION)) {
        Toast.makeText(it, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      } else {
        Toast.makeText(it, e, Toast.LENGTH_SHORT).show()
      }
      Timber.e(e)
    }
  }

  override fun showProgress(show: Boolean) {
    if (null != binding.progressBar) {
      if (show) {
        binding.progressBar.visibility = View.VISIBLE
      } else {
        binding.progressBar.visibility = View.GONE
      }
    }
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}