package com.future.pms.ui.bookingdetail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ADD_NEW_LINE
import com.future.pms.util.Constants.Companion.DISABLED_SLOT
import com.future.pms.util.Constants.Companion.EMPTY_SLOT
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.NULL
import com.future.pms.util.Constants.Companion.SPACING
import com.future.pms.util.Constants.Companion.TAKEN_SLOT
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottom_sheet_content.*
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
  private var SLOTS =
    ("/\$_UUAAU_RR_UU_UU_/" + "________________/" + "_AARAU_UU_UU_UU_/" + "_UUARR_RR_UU_AR_/" + "________________/" + "_URAAU_RA_UU_UU_/" + "_RUUAU_RR_UU_UU_/" + "________________/" + "_UU_AU_RU_UR_UU_/" + "_UU_AU_RR_AR_UU_/" + "________________/" + "_UURAUARRAUUAUU_/" + "________________/" + "_URRAUARARUURUU_/" + "________________/")

  companion object {
    const val TAG: String = Constants.BOOKING_DETAIL_FRAGMENT
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

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_detail, container, false)
    bindingActivityMain = DataBindingUtil.inflate(inflater, R.layout.activity_main, null, false)

    accessToken = Gson().fromJson(
      context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
        Constants.TOKEN, null
      ), Token::class.java
    ).accessToken
    binding.parkingDirectionContent.backBookingDetail.setOnClickListener { backToHome() }
    binding.parkingDirectionContent.buttonScanAgain.setOnClickListener { scanAgain() }
    idBooking = this.arguments?.getString(ID_BOOKING).toString()
    layout = binding.parkingDirectionSheet.layoutPark.findViewById(R.id.layoutPark)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    if (NULL != idBooking) {
      presenter.loadBooking(accessToken)
    } else {
      showProgress(false)
      binding.parkingDirectionContent.errorText.visibility = View.VISIBLE
      binding.parkingDirectionContent.buttonScanAgain.visibility = View.VISIBLE
      binding.parkingDirectionContent.iconBookingDetail.setImageResource(R.drawable.ic_sad)
      binding.parkingDirectionContent.welcomeTo.text = getString(R.string.oops)
      binding.parkingDirectionContent.errorText.text = getString(R.string.failed_create_booking)
      binding.parkingDirectionSheet.root.visibility = View.GONE
    }
  }

  override fun loadBookingSuccess(booking: CustomerBooking) {
    showParkingLayout(layout)
    binding.parkingDirectionSheet.swipeUpIndicator.visibility = View.VISIBLE
    binding.parkingDirectionContent.welcomeTo.text =
      String.format(getString(R.string.welcome_to), booking.parkingZoneName)
    binding.parkingDirectionContent.slotName.text = booking.slotName
    binding.parkingDirectionContent.layoutBookingDetail.visibility = View.VISIBLE
    binding.parkingDirectionContent.iconBookingDetail.setImageResource(R.drawable.ic_smile)
    binding.parkingDirectionContent.dateIn.text = Utils.convertLongToTimeOnly(booking.dateIn)
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
    if (null != progressBar && show) {
      progressBar.visibility = View.VISIBLE
    } else if (null != progressBar && !show) {
      progressBar.visibility = View.GONE
    }
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(ERROR).e(error)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }

  private fun showParkingLayout(layout: HorizontalScrollView) {
    val layoutPark = LinearLayout(context)
    val params = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutPark.orientation = LinearLayout.VERTICAL
    layoutPark.layoutParams = params
    layoutPark.setPadding(
      8 * Constants.parkGaping,
      8 * Constants.parkGaping,
      8 * Constants.parkGaping,
      8 * Constants.parkGaping
    )
    layout.addView(layoutPark)

    var newLayout: LinearLayout? = null
    var count = 0

    for (index in 0 until SLOTS.length) {
      if (SLOTS[index] == ADD_NEW_LINE) {
        newLayout = LinearLayout(context)
        newLayout.orientation = LinearLayout.HORIZONTAL
        layoutPark.addView(newLayout)
      } else if (SLOTS[index] == TAKEN_SLOT) {
        count++
        val view = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(Constants.parkSize, Constants.parkSize)
        layoutParams.setMargins(
          Constants.parkGaping, Constants.parkGaping, Constants.parkGaping, Constants.parkGaping
        )
        view.layoutParams = layoutParams
        view.setPadding(0, 0, 0, 4 * Constants.parkGaping)
        view.id = count
        view.gravity = Gravity.CENTER
        view.setBackgroundResource(R.drawable.ic_car)
        view.setTextColor(Color.WHITE)
        view.tag = Constants.STATUS_BOOKED
        view.text = String.format(getString(R.string.only_placeholder), count.toString())
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
        newLayout!!.addView(view)
        parkViewList.add(view)
        view.setOnClickListener { onClick(view) }
      } else if (SLOTS[index] == EMPTY_SLOT) {
        count++
        val view = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(Constants.parkSize, Constants.parkSize)
        layoutParams.setMargins(
          Constants.parkGaping, Constants.parkGaping, Constants.parkGaping, Constants.parkGaping
        )
        view.layoutParams = layoutParams
        view.setPadding(0, 0, 0, 4 * Constants.parkGaping)
        view.id = count
        view.gravity = Gravity.CENTER
        view.setBackgroundResource(R.drawable.ic_park)
        view.text = String.format(getString(R.string.only_placeholder), count.toString())
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
        view.setTextColor(Color.BLACK)
        view.tag = Constants.STATUS_AVAILABLE
        newLayout!!.addView(view)
        parkViewList.add(view)
        view.setOnClickListener { onClick(view) }
      } else if (SLOTS[index] == DISABLED_SLOT) {
        count++
        val view = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(Constants.parkSize, Constants.parkSize)
        layoutParams.setMargins(
          Constants.parkGaping, Constants.parkGaping, Constants.parkGaping, Constants.parkGaping
        )
        view.layoutParams = layoutParams
        view.setPadding(0, 0, 0, 4 * Constants.parkGaping)
        view.id = count
        view.gravity = Gravity.CENTER
        view.setBackgroundResource(R.drawable.ic_disable)
        view.text = String.format(getString(R.string.only_placeholder), count.toString())
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
        view.setTextColor(Color.WHITE)
        view.tag = Constants.STATUS_RESERVED
        newLayout!!.addView(view)
        parkViewList.add(view)
        view.setOnClickListener { onClick(view) }
      } else if (SLOTS[index] == SPACING) {
        val view = TextView(context)
        val layoutParams = LinearLayout.LayoutParams(Constants.parkSize, Constants.parkSize)
        layoutParams.setMargins(
          Constants.parkGaping, Constants.parkGaping, Constants.parkGaping, Constants.parkGaping
        )
        view.layoutParams = layoutParams
        view.setBackgroundResource(R.drawable.ic_road)
        view.text = getString(R.string.empty)
        newLayout!!.addView(view)
      }
    }
  }

  private fun onClick(view: View) {
    if (view.tag as Int == Constants.STATUS_AVAILABLE) {
      if (Constants.selectedIds.contains(view.id.toString() + ",")) {
        Constants.selectedIds = Constants.selectedIds.replace((+view.id).toString() + ",", "")
        view.setBackgroundResource(R.drawable.ic_car)
      } else {
        Constants.selectedIds = Constants.selectedIds + view.id + ","
        view.setBackgroundResource(R.drawable.ic_my_location)
      }
    } else if (view.tag as Int == Constants.STATUS_BOOKED) {
      Toast.makeText(
        context, "Slot ${view.id} ${getString(R.string.is_booked)}", Toast.LENGTH_SHORT
      ).show()
    } else if (view.tag as Int == Constants.STATUS_RESERVED) {
      Toast.makeText(
        context, "Slot ${view.id} ${getString(R.string.is_reserved)}", Toast.LENGTH_SHORT
      ).show()
    }
  }
}