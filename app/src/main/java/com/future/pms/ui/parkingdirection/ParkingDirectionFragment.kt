package com.future.pms.ui.parkingdirection

import android.content.Context
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
import androidx.fragment.app.Fragment
import com.future.pms.R
import com.future.pms.databinding.FragmentParkingDirectionBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.PARKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.STATUS_AVAILABLE
import com.future.pms.util.Constants.Companion.STATUS_BOOKED
import com.future.pms.util.Constants.Companion.STATUS_RESERVED
import com.future.pms.util.Constants.Companion.STATUS_ROAD
import com.future.pms.util.Constants.Companion.parkMargin
import com.future.pms.util.Constants.Companion.parkPadding
import com.future.pms.util.Constants.Companion.parkSize
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_parking_direction.*
import java.util.*
import javax.inject.Inject

class ParkingDirectionFragment : Fragment(), ParkingDirectionContract {
  @Inject lateinit var presenter: ParkingDirectionPresenter
  private lateinit var binding: FragmentParkingDirectionBinding
  private var parkViewList: MutableList<TextView> = ArrayList()
  private lateinit var layout: HorizontalScrollView
  private lateinit var idBooking: String

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

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parking_direction, container,
        false)
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.attach(this)
    layout = binding.layoutPark
    idBooking = this.arguments?.getString(Constants.ID_BOOKING).toString()
    binding.parkingLevelTitle.text = this.arguments?.getString(Constants.LEVEL_NAME).toString()
    presenter.getParkingLayout(idBooking, accessToken)
    val toolbar = binding.toolbar
    toolbar.setNavigationIcon(R.drawable.ic_back_white)
    toolbar.setNavigationOnClickListener { backToHome() }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.subscribe()
    initView()
  }

  private fun initView() {}

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
      if (index == 0 || totalSlot == 16) {
        totalSlot = 0
        parkingLayout = LinearLayout(context)
        parkingLayout.orientation = LinearLayout.HORIZONTAL
        layoutPark.addView(parkingLayout)
      }

      when {
        slotsLayout[index] == '_' -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_blank)
        }
        slotsLayout[index] == 'S' || slotsLayout[index] == 'T' -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_BOOKED,
              R.drawable.ic_car)
        }
        slotsLayout[index] == 'E' -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_AVAILABLE,
              R.drawable.ic_park)
        }
        slotsLayout[index] == 'D' -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_RESERVED,
              R.drawable.ic_disable)
        }
        slotsLayout[index] == 'R' || slotsLayout[index] == 'O' -> {
          setupParkingView(index, parkingLayout, slotsLayout[index], STATUS_ROAD,
              R.drawable.ic_road)
        }
        slotsLayout[index] == 'V' -> {
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
      if (code != '_') {
        id = count
      }

      if (icon == R.drawable.ic_road) {
        setTextColor(resources.getColor(R.color.colorPrimaryDark))
        text = ((id % 16) + 1).toString()
      }
    }
    layout?.let {
      it.addView(view)
    }
    parkViewList.add(view)
    return view
  }

  override fun getLayoutSuccess(slotsLayout: String) {
    showParkingLayout(slotsLayout)
  }

  override fun getLayoutFailed() {
    TODO(
        "not implemented") //To change body of created functions use File | Settings | File Templates.
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

  private fun backToHome() {
    val activity = activity as MainActivity?
    activity?.presenter?.onHomeIconClick()
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()

    homeComponent.inject(this)
  }
}
