package com.future.pms.ui.superadmin.activitydetails

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.future.pms.R
import com.future.pms.databinding.FragmentBottomSheetActivityBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.superadmin.listactivity.ListActivityFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.LIST_ACTIVITY_FRAGMENT
import com.future.pms.util.Constants.Companion.STATUS_ONGOING
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_receipt.*
import javax.inject.Inject

class ActivityDetailsFragment : BottomSheetDialogFragment(), ActivityDetailsContract {
  @Inject lateinit var presenter: ActivityDetailsPresenter
  private lateinit var binding: FragmentBottomSheetActivityBinding
  private lateinit var accessToken: String
  private lateinit var idBooking: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet_activity, container,
        false)
    presenter.attach(this)
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    binding.btnCheckout.setOnClickListener {
      presenter.checkoutBookingSA(idBooking, accessToken)
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    idBooking = this.arguments?.getString(ID_BOOKING).toString()
    presenter.bookingReceiptSA(idBooking, accessToken)

  }

  override fun showErrorMessage(error: String) {
    Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
  }

  override fun bookingReceiptSASuccess(receipt: Receipt) {
    with(binding) {
      tvCustomerName.text = receipt.customerName
      bookingId.text = receipt.idBooking
      parkingZoneName.text = receipt.parkingZoneName
      address.text = receipt.address
      parkingSlot.text = receipt.slotName
      price.text = String.format(getString(R.string.price_per_hour),
          Utils.thousandSeparator(receipt.price.toInt()))
      inDate.text = Utils.convertLongToTime(receipt.dateIn)
      outDate.text = Utils.convertLongToTime(receipt.dateOut)
      hours.text = receipt.totalHours.toString()
      minutes.text = receipt.totalMinutes.toString()
      totalPrice.text = String.format(getString(R.string.total_price),
          Utils.thousandSeparator(receipt.totalPrice.toInt()))
      if (receipt.status == STATUS_ONGOING) {
        btnCheckout.visibility = View.VISIBLE
      }
    }
  }

  override fun checkoutBookingSASuccess(receipt: Receipt?) {
    val listActivityFragment = fragmentManager?.findFragmentByTag(
        LIST_ACTIVITY_FRAGMENT) as ListActivityFragment
    listActivityFragment.updatedList()
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

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}