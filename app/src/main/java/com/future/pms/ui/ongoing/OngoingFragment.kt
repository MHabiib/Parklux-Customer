package com.future.pms.ui.ongoing

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ongoing.*
import kotlinx.android.synthetic.main.fragment_ongoing.view.*
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class OngoingFragment : Fragment(), OngoingContract {
  @Inject lateinit var presenter: OngoingPresenter
  private lateinit var rootView: View
  private lateinit var parkingTime: Chronometer

  companion object {
    const val TAG: String = Constants.ONGOING_FRAGMENT
  }

  fun newInstance(): OngoingFragment {
    return OngoingFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    rootView = inflater.inflate(R.layout.fragment_ongoing, container, false)
    val directionLayout = rootView.findViewById(R.id.directions_layout) as ConstraintLayout
    directionLayout.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.showParkingDirection()
    }
    parkingTime = rootView.parking_time
    val checkout = rootView.checkout_button as Button
    checkout.setOnClickListener { presenter.checkoutBooking(accessToken) }
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    initView()
  }

  private fun initView() {
    val accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    presenter.loadOngoingBooking(accessToken)
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar && show) {
      progressBar.visibility = View.VISIBLE
    } else {
      progressBar.visibility = View.GONE
    }
  }

  override fun refreshHome() {
    val ft = fragmentManager!!.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft.setReorderingAllowed(false)
    }
    ft.detach(this).attach(this).commit()
  }

  override fun checkoutSuccess(idBooking: String) {
    val activity = activity as MainActivity?
    activity?.presenter?.showReceipt(idBooking)
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(Constants.ERROR).e(error)
  }

  override fun loadCustomerOngoingSuccess(ongoing: CustomerBooking) {
    rootView.dont_have_ongoing.visibility = View.GONE
    rootView.ongoing_parking_layout.visibility = View.VISIBLE
    rootView.parking_zone_name.text = ongoing.parkingZoneName
    rootView.parking_zone_address.text = ongoing.address
    rootView.parking_slot.text = ongoing.slotName
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      parkingTime.base =
        SystemClock.elapsedRealtime() - ((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) - ongoing.dateIn)
    }
    parkingTime.start()
    loadImage(ongoing.imageUrl)
  }

  override fun loadCustomerOngoingFailed() {
    rootView.dont_have_ongoing.visibility = View.VISIBLE
  }

  fun loadImage(imageUrl: String) {
    Glide.with(rootView).load(imageUrl).transform(CenterCrop(), RoundedCorners(80)).placeholder(
      R.drawable.ic_image_place_holder
    ).error(R.drawable.ic_image_place_holder).fallback(
      R.drawable.ic_image_place_holder
    ).into(rootView.ongoing_iv)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }
}