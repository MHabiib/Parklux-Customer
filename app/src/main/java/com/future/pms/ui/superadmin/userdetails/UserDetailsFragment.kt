package com.future.pms.ui.superadmin.userdetails

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.future.pms.R
import com.future.pms.databinding.FragmentBottomSheetUserDetailsBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.model.user.UserResponse
import com.future.pms.ui.superadmin.listuser.ListUserFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ID_USER
import com.future.pms.util.Constants.Companion.LIST_USER_FRAGMENT
import com.future.pms.util.Constants.Companion.ROLE
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UserDetailsFragment : BottomSheetDialogFragment(), UserDetailsContract {
  @Inject lateinit var presenter: UserDetailsPresenter
  private lateinit var binding: FragmentBottomSheetUserDetailsBinding
  private lateinit var accessToken: String
  private lateinit var id: String
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_user_details,
        container, false)
    presenter.attach(this)
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    with(binding) {
      btnEditProfileCustomer.setOnClickListener {
        presenter.updateCustomer(this@UserDetailsFragment.id, profileNameCustomer.text.toString(),
            profileEmailCustomer.text.toString(), profilePasswordCustomer.text.toString(),
            profilePhoneNumberCustomer.text.toString(), accessToken)
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
        presenter.updateAdmin(this@UserDetailsFragment.id, binding.profileNameAdmin.text.toString(),
            binding.profileEmailAdmin.text.toString(),
            binding.profilePhoneNumberAdmin.text.toString(), binding.priceAdmin.text.toString(),
            String.format(getString(R.string.range2), binding.openHourAdmin.text.toString(),
                binding.openHour2Admin.text.toString()), binding.addressAdmin.text.toString(),
            binding.passwordAdmin.text.toString(), accessToken)
      }

      btnDeleteProfileSuperAdmin.setOnClickListener {
        presenter.deleteSuperAdmin(this@UserDetailsFragment.id, accessToken)
      }
      btnEditProfileSuperAdmin.setOnClickListener {
        if (isValid(Constants.UPDATE_SUPER_ADMIN)) {
          presenter.updateSuperAdmin(this@UserDetailsFragment.id, accessToken,
              emailSuperAdmin.text.toString(), passwordSuperAdmin.text.toString(), ROLE_SUPER_ADMIN)
          passwordSuperAdmin.text?.clear()
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
        openHourAdmin.hint = it.openHour.substring(0, 5)
        openHour2Admin.hint = it.openHour.substring(7, 13)
        addressAdmin.setText(it.address)
        passwordAdmin.hint = getString(R.string.password_hint)
      }
    }
  }

  override fun loadDataSuperAdminSuccess(userResponse: UserResponse) {
    with(binding) {
      tvName.text = userResponse.user.email
      id.text = userResponse.user.idUser
      emailSuperAdmin.setText(userResponse.user.email)
      passwordSuperAdmin.hint = getString(R.string.password_hint)
    }
  }

  @SuppressLint("SimpleDateFormat") private fun getDate(textView: TextView, context: Context) {
    val cal = Calendar.getInstance()
    val dateSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
      cal.set(Calendar.HOUR_OF_DAY, hour)
      cal.set(Calendar.MINUTE, minute)
      textView.text = SimpleDateFormat("HH:mm").format(cal.time)
    }

    textView.setOnClickListener {
      TimePickerDialog(context, dateSetListener, cal.get(Calendar.HOUR_OF_DAY),
          cal.get(Calendar.MINUTE), true).show()
    }
  }

  override fun updateCustomerSuccess() {
    presenter.getUpdatedCustomer(id, accessToken)
  }

  override fun updateAdminSuccess() {
    presenter.getUpdatedAdmin(id, accessToken)
  }

  override fun updateSuperAdminSuccess() {
    presenter.getUpdatedSuperAdmin(id, accessToken)
  }

  override fun getUpdatedCustomerSuccess(customer: Customer?) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    customer?.let { listUserFragment.updateCustomerSuccess(it) }
  }

  override fun getUpdatedAdminSuccess(adminResponse: AdminResponse?) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    adminResponse?.let { listUserFragment.updateAdminSuccess(it) }
  }

  override fun getUpdatedSuperAdminSuccess(userResponse: UserResponse) {
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    listUserFragment.updateSuperAdminSuccess(userResponse.user)
  }

  override fun deleteSuperAdminSuccess(response: String?) {
    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
    val listUserFragment = fragmentManager?.findFragmentByTag(
        LIST_USER_FRAGMENT) as ListUserFragment
    listUserFragment.deleteSuperAdminSuccess()
  }

  private fun isValid(role: String): Boolean {
    when (role) {
      ROLE_ADMIN -> {

        return true
      }
      ROLE_SUPER_ADMIN -> {
        if (binding.emailSuperAdmin.toString().isEmpty()) return false
        if (binding.passwordSuperAdmin.toString().isEmpty()) return false
        return true
      }
      else -> {

        return true
      }
    }
  }

  private fun textWatcher(): TextWatcher {
    return object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {}
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //todo
      }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
  }

  override fun onFailed(e: String) {
    if (e.contains(Constants.NO_CONNECTION)) {
      Toast.makeText(context, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
    }
    Timber.e(e)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}