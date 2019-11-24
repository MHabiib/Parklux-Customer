package com.future.pms.ui.parkingdirection

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
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants.Companion.PARKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.util.Constants.Companion.seatGaping
import com.future.pms.util.Constants.Companion.seatSize
import com.future.pms.util.Constants.Companion.selectedIds
import java.util.*
import javax.inject.Inject

class ParkingDirectionFragment : Fragment(), ParkingDirectionContract {
  private var SLOTS =
    ("/\$_UUAAU_RR_UU_UU_/" + "___Z_____________/" + "_AARAU_UU_UU_UU_/" + "_UUARR_RR_UU_AR_/" + "________________/" + "_URAAU_RA_UU_UU_/" + "_RUUAU_RR_UU_UU_/" + "________________/" + "_UU_AU_RU_UR_UU_/" + "_UU_AU_RR_AR_UU_/" + "________________/" + "_UURAUARRAUUAUU_/" + "________________/" + "_URRAUARARUURUU_/" + "________________/")

  @Inject lateinit var presenter: ParkingDirectionPresenter
  private lateinit var rootView: View
  private var seatViewList: MutableList<TextView> = ArrayList()

  fun newInstance(): ParkingDirectionFragment {
    return ParkingDirectionFragment()
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
    rootView = inflater.inflate(R.layout.fragment_parking_direction, container, false)
    val toolbar = rootView.findViewById(R.id.toolbar) as Toolbar
    val layout = rootView.findViewById(R.id.layoutSeat) as HorizontalScrollView
    toolbar.setNavigationIcon(R.drawable.ic_back_white)
    showParkingSlot(layout)
    toolbar.setNavigationOnClickListener { backToHome() }
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    initView()
  }

  private fun initView() {}

  private fun showParkingSlot(layout: HorizontalScrollView) {
    val layoutSeat = LinearLayout(context)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    layoutSeat.orientation = LinearLayout.VERTICAL
    layoutSeat.layoutParams = params
    layoutSeat.setPadding(4 * seatGaping, 4 * seatGaping, 4 * seatGaping, 4 * seatGaping)
    layout.addView(layoutSeat)

    var layout: LinearLayout? = null
    var count = 0

    for (index in 0 until SLOTS.length) {
      when {
        SLOTS[index] == '/' -> {
          layout = LinearLayout(context)
          layout.orientation = LinearLayout.HORIZONTAL
          layoutSeat.addView(layout)
        }
        SLOTS[index] == 'U' -> {
          count++
          val view = TextView(context)
          val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
          layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
          view.layoutParams = layoutParams
          view.setPadding(0, 0, 0, 0)
          view.id = count
          view.gravity = Gravity.CENTER
          view.setBackgroundResource(R.drawable.ic_car)
          view.setTextColor(Color.WHITE)
          view.tag = STATUS_BOOKED
          view.text = count.toString()
          view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
          layout!!.addView(view)
          seatViewList.add(view)
          view.setOnClickListener { onClick(view) }
        }
        SLOTS[index] == 'A' -> {
          count++
          val view = TextView(context)
          val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
          layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
          view.layoutParams = layoutParams
          view.setPadding(0, 0, 0, 2 * seatGaping)
          view.id = count
          view.gravity = Gravity.CENTER
          view.setBackgroundResource(R.drawable.ic_park)
          view.text = count.toString()
          view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
          view.setTextColor(Color.BLACK)
          view.tag = STATUS_AVAILABLE
          layout!!.addView(view)
          seatViewList.add(view)
          view.setOnClickListener { onClick(view) }
        }
        SLOTS[index] == 'R' -> {
          count++
          val view = TextView(context)
          val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
          layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
          view.layoutParams = layoutParams
          view.id = count
          view.gravity = Gravity.CENTER
          view.setBackgroundResource(R.drawable.ic_disable)
          view.text = count.toString()
          view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
          view.setTextColor(Color.WHITE)
          view.tag = STATUS_RESERVED
          layout!!.addView(view)
          seatViewList.add(view)
          view.setOnClickListener { onClick(view) }
        }
        SLOTS[index] == '_' -> {
          val view = TextView(context)
          val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
          layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping)
          view.layoutParams = layoutParams
          view.gravity = Gravity.CENTER
          view.setBackgroundResource(R.drawable.ic_road)
          view.text = ""
          layout!!.addView(view)
        }
      }
    }
  }

  private fun onClick(view: View) {
    if (view.tag as Int == STATUS_AVAILABLE) {
      if (selectedIds.contains(view.id.toString() + ",")) {
        selectedIds = selectedIds.replace((+view.id).toString() + ",", "")
        view.setBackgroundResource(R.drawable.ic_car)
      } else {
        selectedIds = selectedIds + view.id + ","
        view.setBackgroundResource(R.drawable.ic_my_location)
      }
    } else if (view.tag as Int == STATUS_BOOKED) {
      Toast.makeText(context, "Seat " + view.id + " is Booked", Toast.LENGTH_SHORT).show()
    } else if (view.tag as Int == STATUS_RESERVED) {
      Toast.makeText(context, "Seat " + view.id + " is Reserved", Toast.LENGTH_SHORT).show()
    }
  }

  private fun backToHome() {
    val activity = activity as MainActivity?
    activity?.presenter?.onHomeIconClick()
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()

    homeComponent.inject(this)
  }

  companion object {
    const val TAG: String = PARKING_DETAIL_FRAGMENT
  }
}
