package com.future.pms.ui.ongoing

import android.app.Notification
import android.app.NotificationChannel.DEFAULT_CHANNEL_ID
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.R
import com.future.pms.databinding.FragmentOngoingBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ongoing.*
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class OngoingFragment : Fragment(), OngoingContract {
  @Inject lateinit var presenter: OngoingPresenter
  private lateinit var binding: FragmentOngoingBinding
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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ongoing, container, false)
    val directionLayout = binding.directionsLayout
    directionLayout.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.showParkingDirection()
    }
    parkingTime = binding.parkingTime
    val checkout = binding.checkoutButton
    checkout.setOnClickListener { presenter.checkoutBooking(accessToken) }
    return binding.root
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
    val mNotificationManager =
      context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    Utils.createNotificationChannel(mNotificationManager)
    mNotificationManager.notify(1, notificationDetails())
  }

  override fun showProgress(show: Boolean) {
    if (null != progressBar && show) {
      progressBar.visibility = View.VISIBLE
    } else if (null != progressBar && !show) {
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
    binding.dontHaveOngoing.visibility = View.GONE
    binding.ongoingParkingLayout.visibility = View.VISIBLE
    binding.parkingZoneName.text = ongoing.parkingZoneName
    binding.parkingZoneAddress.text = ongoing.address
    binding.parkingSlot.text = ongoing.slotName
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      parkingTime.base =
        SystemClock.elapsedRealtime() - ((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) - ongoing.dateIn)
    }
    parkingTime.start()
    loadImage(ongoing.imageUrl)
  }

  override fun loadCustomerOngoingFailed() {
    binding.dontHaveOngoing.visibility = View.VISIBLE
  }

  private fun loadImage(imageUrl: String) {
    Glide.with(binding.root).load(imageUrl).transform(CenterCrop(), RoundedCorners(80)).placeholder(
      R.drawable.ic_image_place_holder
    ).error(R.drawable.ic_image_place_holder).fallback(
      R.drawable.ic_image_place_holder
    ).into(binding.ongoingIv)
  }

  private fun notificationDetails(): Notification {
    return NotificationCompat.Builder(context!!, DEFAULT_CHANNEL_ID)
      .setContentTitle("Check out parking success")
      .setContentText("Thank you for using Parking Management System apps !")
      .setSmallIcon(R.drawable.logo_blue).setStyle(
        NotificationCompat.BigTextStyle().bigText("Thank you for using Parking Management System apps !")
      ).build()
    //todo
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }
}