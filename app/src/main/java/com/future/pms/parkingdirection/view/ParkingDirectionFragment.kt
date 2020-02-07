package com.future.pms.parkingdirection.view

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
import androidx.fragment.app.Fragment
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentParkingDirectionBinding
import com.future.pms.main.view.MainActivity
import com.future.pms.parkingdirection.injection.DaggerParkingDirectionComponent
import com.future.pms.parkingdirection.injection.ParkingDirectionComponent
import com.future.pms.parkingdirection.presenter.ParkingDirectionPresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.PARKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.SLOT_BLOCK
import com.future.pms.util.Constants.Companion.SLOT_IN
import com.future.pms.util.Constants.Companion.SLOT_NULL
import com.future.pms.util.Constants.Companion.SLOT_OUT
import com.future.pms.util.Constants.Companion.SLOT_READY
import com.future.pms.util.Constants.Companion.SLOT_ROAD
import com.future.pms.util.Constants.Companion.parkMargin
import com.future.pms.util.Constants.Companion.parkPadding
import com.future.pms.util.Constants.Companion.parkSize
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_parking_direction.*
import timber.log.Timber
import javax.inject.Inject

class ParkingDirectionFragment : Fragment(), ParkingDirectionContract {
  private var daggerBuild: ParkingDirectionComponent = DaggerParkingDirectionComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ParkingDirectionPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentParkingDirectionBinding
  private lateinit var layout: HorizontalScrollView
  private lateinit var idBooking: String
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = PARKING_DETAIL_FRAGMENT
  }

  fun newInstance(): ParkingDirectionFragment = ParkingDirectionFragment()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      val activity = activity as MainActivity?
      activity?.presenter?.onHomeIconClick()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parking_direction, container,
        false)

    layout = binding.layoutPark
    idBooking = this.arguments?.getString(Constants.ID_BOOKING).toString()
    binding.parkingLevelTitle.text = this.arguments?.getString(Constants.LEVEL_NAME).toString()

    val toolbar = binding.toolbar
    toolbar.setNavigationIcon(R.drawable.ic_back_white)
    toolbar.setNavigationOnClickListener { backToHome() }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    showProgress(true)
    presenter.getParkingLayout(idBooking, accessToken)
  }

  private fun showParkingLayout(slotsLayout: String) {
    val layoutPark = LinearLayout(context)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)

    layoutPark.apply {
      orientation = LinearLayout.VERTICAL
      layoutParams = params
      setPadding(parkPadding, parkPadding, parkPadding, parkPadding)
    }
    layout.addView(layoutPark)

    CountLayout().execute(slotsLayout)

    showProgress(false)
  }

  private inner class CountLayout : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg params: String?): String? {
      var int = ""
      for (index in params.indices) {
        int = "$index$params"
      }
      //todo
      return int
    }

    override fun onPostExecute(result: String?) {
      super.onPostExecute(result)
      setUppppppp(result)
    }

  }

  fun setUppppppp(result: String?) {
    val slotsLayout = result?.substring(result.length - 1)?.single()
    val index = result?.substring(0, result.length-1)?.toInt()

    val parkingLayout: LinearLayout?
    if (index != null) {
      if (index == 0 || index % 60 == 0) {
        parkingLayout = LinearLayout(context)
        parkingLayout.orientation = LinearLayout.HORIZONTAL
        layoutPark.addView(parkingLayout)

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
          SLOT_ROAD, SLOT_READY -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.color.transparent)
          }
          SLOT_IN -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_in)
          }
          SLOT_OUT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_out)
          }
          SLOT_BLOCK -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_road)
          }
          Constants.MY_SLOT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_my_location)
          }
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
        text = ((id % 60) + 1).toString()
        setTypeface(this.typeface, Typeface.BOLD)
      }
    }
    layout?.post {
      layout.addView(view)
    }
  }

  override fun getLayoutSuccess(slotsLayout: String) = showParkingLayout(slotsLayout)

  override fun onFailed(message: String) {
    progressBar.visibility = View.GONE
    binding.ibRefresh.visibility = View.VISIBLE
    binding.ibRefresh.setOnClickListener {
      progressBar.visibility = View.VISIBLE
      binding.ibRefresh.visibility = View.GONE
      showProgress(true)
      presenter.getParkingLayout(idBooking, accessToken)
    }
    Timber.tag(Constants.ERROR).e(message)
  }

  override fun showProgress(show: Boolean) {
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun backToHome() {
    val activity = activity as MainActivity?
    activity?.presenter?.onHomeIconClick()
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}
