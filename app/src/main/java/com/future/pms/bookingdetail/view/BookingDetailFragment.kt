package com.future.pms.bookingdetail.view

import android.content.Context
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.bookingdetail.injection.BookingDetailComponent
import com.future.pms.bookingdetail.injection.DaggerBookingDetailComponent
import com.future.pms.bookingdetail.presenter.BookingDetailPresenter
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.CustomerBooking
import com.future.pms.core.model.Token
import com.future.pms.databinding.ActivityMainBinding
import com.future.pms.databinding.FragmentBookingDetailBinding
import com.future.pms.main.view.MainActivity
import com.future.pms.ongoing.view.OngoingFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.AUTHENTICATION
import com.future.pms.util.Constants.Companion.BOOKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.NULL
import com.future.pms.util.Constants.Companion.SLOTS_IN_ROW
import com.future.pms.util.Constants.Companion.SLOT_NULL
import com.future.pms.util.Constants.Companion.TOKEN
import com.future.pms.util.Constants.Companion.parkMargin
import com.future.pms.util.Constants.Companion.parkPadding
import com.future.pms.util.Constants.Companion.parkSize
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_parking_direction.*
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class BookingDetailFragment : BaseFragment(), BookingDetailContract {
  private var daggerBuild: BookingDetailComponent = DaggerBookingDetailComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: BookingDetailPresenter
  @Inject lateinit var gson: Gson
  private lateinit var idBooking: String
  private lateinit var accessToken: String
  private lateinit var layout: HorizontalScrollView
  private lateinit var binding: FragmentBookingDetailBinding
  private lateinit var bindingActivityMain: ActivityMainBinding
  private lateinit var layoutPark: LinearLayout
  private lateinit var parkingLayout: LinearLayout
  private lateinit var asyncTask: SetupLayoutAsyc
  private var totalRow = 0

  companion object {
    const val TAG: String = BOOKING_DETAIL_FRAGMENT
  }

  fun newInstance(): BookingDetailFragment = BookingDetailFragment()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val ongoingFragment = fragmentManager?.findFragmentByTag(
          OngoingFragment.TAG) as OngoingFragment
      val activity = activity as MainActivity?

      ongoingFragment.refreshPage()
      activity?.presenter?.onHomeIconClick()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail, container, false)
    bindingActivityMain = DataBindingUtil.inflate(inflater, R.layout.activity_main, null, false)

    with(binding) {
      parkingDirectionContent.backBookingDetail.setOnClickListener { backToHome() }
      parkingDirectionContent.buttonScanAgain.setOnClickListener { scanAgain() }
      layout = parkingDirectionSheet.layoutPark.findViewById(R.id.layoutPark)
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()

    accessToken = gson.fromJson(
        context?.getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    idBooking = this.arguments?.getString(ID_BOOKING).toString()

    if (NULL != idBooking) {
      presenter.loadBooking(accessToken)
      presenter.getParkingLayout(idBooking, accessToken)
      showProgress(true)
    } else {
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

  override fun getLayoutSuccess(slotsLayout: String) = showParkingLayout(slotsLayout)

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
  }

  private fun backToHome() {
    val activity = activity as MainActivity?
    val ongoingFragment = fragmentManager?.findFragmentByTag(OngoingFragment.TAG) as OngoingFragment

    activity?.presenter?.onHomeIconClick()
    ongoingFragment.refreshPage()
  }

  private fun scanAgain() {
    val activity = activity as MainActivity?
    activity?.presenter?.onScanIconClick()
  }

  override fun showProgress(show: Boolean) {
    if (show) {
      binding.parkingDirectionContent.progressBar.visibility = View.VISIBLE
    } else {
      binding.parkingDirectionContent.progressBar.visibility = View.GONE
    }
  }

  override fun showErrorMessage(error: String) = Timber.tag(ERROR).e(error)

  private fun showParkingLayout(slotsLayout: String) {
    layoutPark = LinearLayout(context)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)

    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(parkPadding, parkPadding, parkPadding, parkPadding)
    }
    layout.addView(layoutPark)


    totalRow = 0
    asyncTask = SetupLayoutAsyc(activity as MainActivity)
    asyncTask.execute(slotsLayout)
  }

  private class SetupLayoutAsyc internal constructor(context: MainActivity) :
      AsyncTask<String, String, String>() {
    private val activityReference: WeakReference<MainActivity> = WeakReference(context)
    private val bookingDetailFragment = activityReference.get()?.supportFragmentManager?.run {
      findFragmentByTag(TAG)
    }
    private val mBookingDetailFragment = bookingDetailFragment as BookingDetailFragment

    override fun doInBackground(vararg params: String?): String? {
      val slots = params[0]
      if (slots != null) {
        var index = 0
        while (index in 0 until slots.length) {
          if (index == 1800 && slots[1800] == '_' && slots[1830] == '_') {
            index = slots.length - 1
          }
          if (index % 30 == 0 && slots[index] == '_' && index != 0 && index % 60 != 0) {
            index += 29
          }
          if (index % 60 == 0) {
            Thread.sleep(100)
          }
          publishProgress("$index${slots[index]}")
          index++
        }
      }
      return ""
    }

    override fun onProgressUpdate(vararg result: String?) {
      if (bookingDetailFragment != null && activityReference.get() != null) {
        mBookingDetailFragment.setSlotStatus(result[0])
      }
    }
  }

  fun setSlotStatus(result: String?) {
    if (result != "" && context != null) {
      val slotsLayout = result?.substring(result.length - 1)?.single()
      val index = result?.substring(0, result.length - 1)?.toInt()

      if (index != null) {
        if (index == 0 || index % SLOTS_IN_ROW == 0) {
          parkingLayout = LinearLayout(context)
          parkingLayout.orientation = LinearLayout.HORIZONTAL
          layoutPark.addView(parkingLayout)
          totalRow++
        }

        when (slotsLayout) {
          SLOT_NULL -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_blank)
          }
          Constants.SLOT_SCAN_ME, Constants.SLOT_TAKEN -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_car)
          }
          Constants.SLOT_EMPTY -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_park)
          }
          Constants.DISABLED_SLOT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_disable)
          }
          Constants.SLOT_ROAD, Constants.SLOT_READY -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.color.transparent)
          }
          Constants.SLOT_IN -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_in)
          }
          Constants.SLOT_OUT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_out)
          }
          Constants.SLOT_BLOCK -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_road)
          }
          Constants.MY_SLOT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_my_location)
          }
        }
        if (totalRow == 30) {
          binding.parkingDirectionSheet.numberingLeft.visibility = View.GONE
          binding.parkingDirectionSheet.numberingLeftOneRow.visibility = View.VISIBLE
          showProgress(false)
        }
        if (totalRow == 31) {
          binding.parkingDirectionSheet.numberingLeft.visibility = View.VISIBLE
          binding.parkingDirectionSheet.numberingLeftOneRow.visibility = View.GONE
          showProgress(true)
        }
        if (totalRow == 60) {
          showProgress(false)
        }
      }
    }
  }

  private fun setupParkingView(count: Int, layout: LinearLayout?, code: Char, icon: Int) {
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

      if (icon == R.drawable.ic_park || icon == R.drawable.ic_disable || icon == R.drawable.ic_car) {
        setTextColor(resources.getColor(R.color.darkGrey))
        text = ((id % SLOTS_IN_ROW) + 1).toString()
        setTypeface(this.typeface, Typeface.BOLD)
      }
    }
    layout?.addView(view)
  }

  override fun onFailed(message: String) = Timber.tag("e").e(message)

  override fun onDestroy() {
    if (::asyncTask.isInitialized) {
      asyncTask.cancel(true)
    }
    presenter.detach()
    super.onDestroy()
  }
}