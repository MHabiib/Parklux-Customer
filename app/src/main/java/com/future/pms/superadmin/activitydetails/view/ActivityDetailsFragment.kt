package com.future.pms.superadmin.activitydetails.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.model.Receipt
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentBottomSheetActivityBinding
import com.future.pms.superadmin.activitydetails.injection.ActivityDetailsComponent
import com.future.pms.superadmin.activitydetails.injection.DaggerActivityDetailsComponent
import com.future.pms.superadmin.activitydetails.presenter.ActivityDetailsPresenter
import com.future.pms.superadmin.listactivity.view.ListActivityFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.LIST_ACTIVITY_FRAGMENT
import com.future.pms.util.Constants.Companion.STATUS_ONGOING
import com.future.pms.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import javax.inject.Inject

class ActivityDetailsFragment : BottomSheetDialogFragment(), ActivityDetailsContract {
  private var daggerBuild: ActivityDetailsComponent = DaggerActivityDetailsComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ActivityDetailsPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentBottomSheetActivityBinding
  private lateinit var accessToken: String
  private lateinit var idBooking: String

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
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    idBooking = this.arguments?.getString(ID_BOOKING).toString()

    presenter.bookingReceiptSA(idBooking, accessToken)
  }

  override fun onFailed(message: String) {
    context?.let {
      Toast.makeText(it, getString(R.string.error), Toast.LENGTH_LONG).show()
    }
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
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}