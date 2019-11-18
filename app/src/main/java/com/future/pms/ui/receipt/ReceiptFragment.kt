package com.future.pms.ui.receipt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.model.receipt.Receipt
import com.future.pms.ui.home.HomeFragment.Companion.idBooking
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.RECEIPT_FRAGMENT
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_receipt.*
import kotlinx.android.synthetic.main.fragment_receipt.view.*
import javax.inject.Inject

class ReceiptFragment : Fragment(), ReceiptContract {
  @Inject lateinit var presenter: ReceiptPresenter
  private lateinit var rootView: View

  fun newInstance(): ReceiptFragment {
    return ReceiptFragment()
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
    rootView = inflater.inflate(R.layout.fragment_receipt, container, false)
    val backButton = rootView.findViewById(R.id.button_back_receipt) as ImageButton
    backButton.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(
        Constants.AUTHENTCATION, Context.MODE_PRIVATE
      )?.getString(Constants.TOKEN, null), Token::class.java
    ).access_token
    presenter.attach(this)
    presenter.subscribe()
    idBooking.let { presenter.loadData(accessToken, it) }
    initView()
  }

  private fun initView() {
    //TODO
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

  override fun loadReceiptSuccess(receipt: Receipt) {
    println(receipt)
    rootView.booking_id.text = receipt.idBooking
    rootView.parking_zone_name.text = receipt.parkingZoneName
    rootView.address.text = receipt.address
    rootView.parking_slot.text = receipt.slotName
    rootView.price.text = String.format("IDR %s0/hour", receipt.price.toString())
    rootView.in_date.text = Utils.convertLongToTime(receipt.dateIn)
    rootView.out_date.text = Utils.convertLongToTime(receipt.dateOut)
    rootView.hours.text = receipt.totalHours.toString()
    rootView.minutes.text = receipt.totalMinutes.toString()
    rootView.total_price.text = String.format("IDR %s0", receipt.totalPrice)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }

  companion object {
    const val TAG: String = RECEIPT_FRAGMENT
  }
}