package com.future.pms.ui.bookingdetail

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.databinding.ActivityMainBinding
import com.future.pms.databinding.FragmentBookingDetailBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants.Companion.AUTHENTCATION
import com.future.pms.util.Constants.Companion.BOOKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.DISABLED_SLOT
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.MY_SLOT
import com.future.pms.util.Constants.Companion.NULL
import com.future.pms.util.Constants.Companion.SLOTS_IN_ROW
import com.future.pms.util.Constants.Companion.SLOT_EMPTY
import com.future.pms.util.Constants.Companion.SLOT_NULL
import com.future.pms.util.Constants.Companion.SLOT_READY
import com.future.pms.util.Constants.Companion.SLOT_ROAD
import com.future.pms.util.Constants.Companion.SLOT_SCAN_ME
import com.future.pms.util.Constants.Companion.SLOT_TAKEN
import com.future.pms.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.util.Constants.Companion.STATUS_ROAD
import com.future.pms.util.Constants.Companion.TOKEN
import com.future.pms.util.Constants.Companion.parkMargin
import com.future.pms.util.Constants.Companion.parkPadding
import com.future.pms.util.Constants.Companion.parkSize
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottom_sheet_content.progressBar
import kotlinx.android.synthetic.main.fragment_parking_direction.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class BookingDetailFragment : Fragment(), BookingDetailContract {
  private var parkViewList: MutableList<TextView> = ArrayList()
  @Inject lateinit var presenter: BookingDetailPresenter
  private lateinit var idBooking: String
  private lateinit var accessToken: String
  private lateinit var layout: HorizontalScrollView
  private lateinit var binding: FragmentBookingDetailBinding
  private lateinit var bindingActivityMain: ActivityMainBinding

  companion object {
    const val TAG: String = BOOKING_DETAIL_FRAGMENT
  }

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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail, container, false)
    bindingActivityMain = DataBindingUtil.inflate(inflater, R.layout.activity_main, null, false)

    accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    idBooking = this.arguments?.getString(ID_BOOKING).toString()
    with(binding) {
      parkingDirectionContent.backBookingDetail.setOnClickListener { backToHome() }
      parkingDirectionContent.buttonScanAgain.setOnClickListener { scanAgain() }
      layout = parkingDirectionSheet.layoutPark.findViewById(R.id.layoutPark)
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    if (NULL != idBooking) {
      presenter.loadBooking(accessToken)
      presenter.getParkingLayout(idBooking, accessToken)
    } else {
      showProgress(false)
      with(binding) {
        parkingDirectionContent.apply {
          errorText.visibility = View.VISIBLE
          buttonScanAgain.visibility = View.VISIBLE
          iconBookingDetail.setImageResource(R.drawable.ic_sad)
          welcomeTo.text = getString(R.string.oops)
          errorText.text = getString(R.string.failed_create_booking)
        }
        parkingDirectionSheet.root.visibility = View.GONE
      }
    }
  }

  override fun getLayoutSuccess(slotsLayout: String) {
    showParkingLayout(slotsLayout)
  }

  override fun loadBookingSuccess(booking: CustomerBooking) {
    with(binding) {
      parkingDirectionContent.apply {
        welcomeTo.text = String.format(getString(R.string.welcome_to), booking.parkingZoneName)
        slotName.text = booking.slotName
        price.text = String.format(getString(R.string.price_per_hour),
            Utils.thousandSeparator(booking.price.toInt()))
        layoutBookingDetail.visibility = View.VISIBLE
        iconBookingDetail.setImageResource(R.drawable.ic_smile)
        dateIn.text = Utils.convertLongToTimeOnly(booking.dateIn)
        parking_level_title.text = booking.levelName
      }
      parkingDirectionSheet.swipeUpIndicator.visibility = View.VISIBLE
    }
    hideItem()
  }

  private fun hideItem() {
    val navigationView = bindingActivityMain.navView
    navigationView.menu.findItem(R.id.navigation_home).isVisible = false
  }

  private fun backToHome() {
    val activity = activity as MainActivity?
    activity?.presenter?.onHomeIconClick()
  }

  private fun scanAgain() {
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
    Timber.tag(ERROR).e(error)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }

  private fun showParkingLayout(slotsLayout: String) {
    val layoutPark = LinearLayout(context)
    var parkingLayout: LinearLayout? = null
    var totalSlot = 0
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(parkPadding, parkPadding, parkPadding, parkPadding)
    }
    layout.addView(layoutPark)

    for (index in 0 until slotsLayout.length) {
      totalSlot++
      if (index == 0 || totalSlot == SLOTS_IN_ROW) {
        totalSlot = 0
        parkingLayout = LinearLayout(context)
        parkingLayout.orientation = LinearLayout.HORIZONTAL
        layoutPark.addView(parkingLayout)
      }

      when {
        slotsLayout[index] == SLOT_NULL -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_blank)
        }
        slotsLayout[index] == SLOT_SCAN_ME || slotsLayout[index] == SLOT_TAKEN -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_BOOKED,
              R.drawable.ic_car)
        }
        slotsLayout[index] == SLOT_EMPTY -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_AVAILABLE,
              R.drawable.ic_park)
        }
        slotsLayout[index] == DISABLED_SLOT -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_RESERVED,
              R.drawable.ic_disable)
        }
        slotsLayout[index] == SLOT_ROAD || slotsLayout[index] == SLOT_READY -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_road)
        }
        slotsLayout[index] == MY_SLOT -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_AVAILABLE,
              R.drawable.ic_my_location)
        }
      }
    }
  }

  private fun setupParkingView(count: Int, layout: LinearLayout?, code: Char, tags: Int,
      icon: Int): TextView {
    val view = TextView(context)
    view.apply {
      layoutParams = LinearLayout.LayoutParams(parkSize, parkSize).apply {
        setMargins(parkMargin, parkMargin, parkMargin, parkMargin)
      }
      setPadding(0, 0, 0, 0)
      gravity = Gravity.CENTER
      setBackgroundResource(icon)
      if (code != SLOT_NULL) {
        id = count
      }

      if (icon == R.drawable.ic_road) {
        setTextColor(resources.getColor(R.color.colorPrimaryDark))
        text = ((id % SLOTS_IN_ROW) + 1).toString()
      }
      setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
    }
    layout?.let {
      it.addView(view)
    }
    parkViewList.add(view)
    return view
  }
}