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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.databinding.FragmentParkingDirectionBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants.Companion.PARKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.util.Constants.Companion.STATUS_ROAD
import com.future.pms.util.Constants.Companion.parkGaping
import com.future.pms.util.Constants.Companion.parkSize
import com.future.pms.util.Constants.Companion.selectedIds
import java.util.*
import javax.inject.Inject

class ParkingDirectionFragment : Fragment(), ParkingDirectionContract {
  @Inject lateinit var presenter: ParkingDirectionPresenter
  private lateinit var binding: FragmentParkingDirectionBinding
  private var parkViewList: MutableList<TextView> = ArrayList()
  private var SLOTS =
    ("/\$_UUAAU_RR_UU_UU_/" + "___Z_____________/" + "_AARAU_UU_UU_UU_/" + "_UUARR_RR_UU_AR_/" + "________________/" + "_URAAU_RA_UU_UU_/" + "_RUUAU_RR_UU_UU_/" + "________________/" + "_UU_AU_RU_UR_UU_/" + "_UU_AU_RR_AR_UU_/" + "________________/" + "_UURAUARRAUUAUU_/" + "________________/" + "_URRAUARARUURUU_/" + "________________/")

  companion object {
    const val TAG: String = PARKING_DETAIL_FRAGMENT
  }

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

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    binding =
      DataBindingUtil.inflate(inflater, R.layout.fragment_parking_direction, container, false)
    val toolbar = binding.toolbar
    val layout = binding.layoutPark
    toolbar.setNavigationIcon(R.drawable.ic_back_white)
    showParkingSlot(layout)
    toolbar.setNavigationOnClickListener { backToHome() }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    initView()
  }

  private fun initView() {}

  private fun showParkingSlot(layout: HorizontalScrollView) {
    val layoutPark = LinearLayout(context)
    var parkingLayout: LinearLayout? = null
    var count = 0
    val params = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )
    layoutPark.orientation = LinearLayout.VERTICAL
    layoutPark.layoutParams = params
    layoutPark.setPadding(4 * parkGaping, 4 * parkGaping, 4 * parkGaping, 4 * parkGaping)
    layout.addView(layoutPark)

    for (index in 0 until SLOTS.length) {
      when {
        SLOTS[index] == '/' -> {
          parkingLayout = LinearLayout(context)
          parkingLayout.orientation = LinearLayout.HORIZONTAL
          layoutPark.addView(parkingLayout)
        }
        SLOTS[index] == 'U' -> {
          count++
          setupParkingView(count, parkingLayout, SLOTS[index], STATUS_BOOKED, R.drawable.ic_car)
        }
        SLOTS[index] == 'A' -> {
          count++
          setupParkingView(count, parkingLayout, SLOTS[index], STATUS_AVAILABLE, R.drawable.ic_park)
        }
        SLOTS[index] == 'R' -> {
          count++
          setupParkingView(
            count, parkingLayout, SLOTS[index], STATUS_RESERVED, R.drawable.ic_disable
          )
        }
        SLOTS[index] == '_' -> {
          setupParkingView(count, parkingLayout, SLOTS[index], STATUS_ROAD, R.drawable.ic_road)
        }
      }
    }
  }

  private fun setupParkingView(
    count: Int, layout: LinearLayout?, code: Char, tag: Int, icon: Int
  ): TextView {
    val view = TextView(context)
    val layoutParams = LinearLayout.LayoutParams(parkSize, parkSize)
    layoutParams.setMargins(parkGaping, parkGaping, parkGaping, parkGaping)
    view.layoutParams = layoutParams
    view.setPadding(0, 0, 0, 0)
    view.gravity = Gravity.CENTER
    view.setBackgroundResource(icon)
    view.setTextColor(Color.WHITE)
    view.tag = tag
    if (code != '_') {
      view.id = count
      view.text = count.toString()
      view.setOnClickListener { onClick(view) }
    } else {
      view.text = ""
    }
    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
    layout!!.addView(view)
    parkViewList.add(view)
    return view
  }

  private fun onClick(view: View) {
    if (view.tag as Int == STATUS_AVAILABLE) {
      if (selectedIds.contains(view.id.toString() + ",")) {
        selectedIds = selectedIds.replace((+view.id).toString() + ",", "")
        view.setBackgroundResource(R.drawable.ic_park)
      } else {
        selectedIds = selectedIds + view.id + ","
        view.setBackgroundResource(R.drawable.ic_my_location)
      }
    } else if (view.tag as Int == STATUS_BOOKED) {
      Toast.makeText(
        context,
        String.format(getString(R.string.park_is_booked), view.id),
        Toast.LENGTH_SHORT
      ).show()
    } else if (view.tag as Int == STATUS_RESERVED) {
      Toast.makeText(
        context,
        String.format(getString(R.string.park_is_reserved), view.id),
        Toast.LENGTH_SHORT
      ).show()
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
}
