package com.future.pms.ui.bookingdetail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Utils
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_booking_detail.view.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_content.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_content.view.*
import javax.inject.Inject

class BookingDetailFragment : Fragment(), BookingDetailContract {
  private lateinit var rootView: View
  private var idBooking: String = ""
  private var accessToken: String = ""
  @Inject lateinit var presenter: BookingDetailPresenter

  fun newInstance(): BookingDetailFragment {
    return BookingDetailFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).access_token
    rootView = inflater.inflate(R.layout.fragment_booking_detail, container, false)
    rootView.findViewById<ImageButton>(R.id.back_booking_detail).setOnClickListener { backToHome() }
    rootView.findViewById<Button>(R.id.button_scan_again).setOnClickListener { scanAgain() }
    idBooking = this.arguments?.getString("idBooking").toString()
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    if ("null" != idBooking) {
      presenter.loadBooking(accessToken)
    } else {
      showProgress(false)
      rootView.error_text.visibility = View.VISIBLE
      rootView.button_scan_again.visibility = View.VISIBLE
      rootView.parking_direction_sheet.visibility = View.GONE
      rootView.icon_booking_detail.setImageResource(R.drawable.ic_sad)
      rootView.welcome_to.text = "Oops..."
      rootView.error_text.text = "Failed to create booking, something went wrong."
    }
  }

  override fun loadBookingSuccess(booking: CustomerBooking) {
    rootView.welcome_to.text = String.format("Welcome to %s", booking.parkingZoneName)
    rootView.slot_name.text = booking.slotName
    rootView.layout_booking_detail.visibility = View.VISIBLE
    rootView.icon_booking_detail.setImageResource(R.drawable.ic_smile)
    rootView.date_in.text = Utils.convertLongToTimeOnly(booking.dateIn)
    hideItem()
  }

  private fun hideItem() {
    val navigationView = rootView.findViewById(R.id.nav_view) as NavigationView
    navigationView.menu.findItem(R.id.navigation_home).isVisible = false
  }

  fun backToHome() {
    val activity = activity as MainActivity?
    activity?.presenter?.onHomeIconClick()
  }

  fun scanAgain() {
    val activity = activity as MainActivity?
    activity?.presenter?.onScanIconClick()
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
    Log.e(ERROR, error)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }

  companion object {
    const val TAG: String = Constants.BOOKING_DETAIL_FRAGMENT
  }
}