package com.future.pms.ongoing.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.CustomerBooking
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentOngoingBinding
import com.future.pms.history.view.HistoryFragment
import com.future.pms.home.view.HomeFragment
import com.future.pms.main.view.MainActivity
import com.future.pms.ongoing.injection.DaggerOngoingComponent
import com.future.pms.ongoing.injection.OngoingComponent
import com.future.pms.ongoing.presenter.OngoingPresenter
import com.future.pms.receipt.view.ReceiptFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.SEC_IN_DAY
import com.future.pms.util.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_ongoing.*
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.ceil

class OngoingFragment : BaseFragment(), OngoingContract {
  private var daggerBuild: OngoingComponent = DaggerOngoingComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: OngoingPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentOngoingBinding
  private lateinit var idBooking: String
  private lateinit var levelName: String
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = Constants.ONGOING_FRAGMENT
  }

  fun newInstance(): OngoingFragment = OngoingFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ongoing, container, false)

    val directionLayout = binding.directionsLayout

    directionLayout.setOnClickListener {
      val activity = activity as MainActivity?
      activity?.presenter?.showParkingDirection(idBooking, levelName)
    }

    val checkout = binding.checkoutButton
    checkout.setOnClickListener { presenter.checkoutBooking(accessToken) }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.attach(this)
    presenter.subscribe()
    presenter.loadOngoingBooking(accessToken)
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

  override fun refreshHome() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  override fun checkoutSuccess(idBooking: String) {
    val fragment = ReceiptFragment()
    val bundle = Bundle()
    bundle.putString(Constants.ID_BOOKING, idBooking)
    fragment.arguments = bundle

    activity?.supportFragmentManager?.let { bottomSheetFragment ->
      if (!fragment.isAdded) {
        fragment.show(bottomSheetFragment, fragment.tag)
      }
    }

    val historyFragment = fragmentManager?.findFragmentByTag(HistoryFragment.TAG) as HistoryFragment
    historyFragment.refreshListHistory()
  }

  override fun onFailed(message: String) {
    val fab = activity?.findViewById(R.id.fab_scan) as FloatingActionButton
    val activity = activity as MainActivity

    fab.setOnClickListener {
      activity.presenter.onScanIconClick()
    }
    Timber.tag(Constants.ERROR).e(message)
  }

  override fun loadCustomerOngoingSuccess(ongoing: CustomerBooking) {
    with(binding) {
      idBooking = ongoing.idBooking
      levelName = ongoing.levelName
      dontHaveOngoing.visibility = View.GONE
      ongoingParkingLayout.visibility = View.VISIBLE
      parkingZoneName.text = ongoing.parkingZoneName
      parkingZoneAddress.text = ongoing.address
      bookingIdValue.text = ongoing.idBooking
      parkingSlot.text = ongoing.slotName
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      with(binding) {
        yourPrice.visibility = View.VISIBLE
        yourPriceTag.visibility = View.VISIBLE
        line1.visibility = View.VISIBLE
        pricePerHourTag.visibility = View.VISIBLE
        pricePerHour.visibility = View.VISIBLE
        pricePerHour.text = String.format(getString(R.string.total_price),
            Utils.thousandSeparator(ongoing.price.toInt()))
        parkingTime.base = SystemClock.elapsedRealtime() - ((LocalDateTime.now().atZone(
            ZoneId.systemDefault()).toInstant().toEpochMilli()) - ongoing.dateIn)
        parkingTime.start()
        parkingTime.setOnChronometerTickListener {
          val elapsedMillis = SystemClock.elapsedRealtime() - it.base
          yourPrice.text = String.format(getString(R.string.idr), Utils.thousandSeparator(
              (ceil(elapsedMillis.toDouble() / SEC_IN_DAY) * ongoing.price).toInt()))
        }
      }
    }

    val fab = activity?.findViewById(R.id.fab_scan) as FloatingActionButton
    fab.setOnClickListener {
      Toast.makeText(context, getString(R.string.only_have_booking_one), Toast.LENGTH_LONG).show()
    }

    loadImage(ongoing.imageUrl)
  }

  override fun loadCustomerOngoingFailed(error: String) {
    if (error.contains(Constants.NO_CONNECTION)) {
      Toast.makeText(context, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show()
      binding.ibRefresh.visibility = View.VISIBLE
      binding.ibRefresh.setOnClickListener {
        presenter.loadOngoingBooking(accessToken)
        binding.ibRefresh.visibility = View.GONE
        binding.dontHaveOngoing.visibility = View.GONE
        val homeFragment = fragmentManager?.findFragmentByTag(HomeFragment.TAG) as HomeFragment
        homeFragment.presenter.loadData(accessToken)
      }
    }

    Timber.tag(Constants.ERROR).e(error)
    binding.dontHaveOngoing.visibility = View.VISIBLE
  }

  private fun loadImage(imageUrl: String) = Utils.imageLoader(binding.root, imageUrl,
      binding.ongoingIv)

  fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}