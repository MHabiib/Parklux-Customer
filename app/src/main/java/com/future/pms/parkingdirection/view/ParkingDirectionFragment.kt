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
import com.future.pms.util.Constants.Companion.DISABLED_SLOT
import com.future.pms.util.Constants.Companion.MY_SLOT
import com.future.pms.util.Constants.Companion.PARKING_DETAIL_FRAGMENT
import com.future.pms.util.Constants.Companion.SLOTS_IN_ROW
import com.future.pms.util.Constants.Companion.SLOT_BLOCK
import com.future.pms.util.Constants.Companion.SLOT_EMPTY
import com.future.pms.util.Constants.Companion.SLOT_IN
import com.future.pms.util.Constants.Companion.SLOT_NULL
import com.future.pms.util.Constants.Companion.SLOT_OUT
import com.future.pms.util.Constants.Companion.SLOT_READY
import com.future.pms.util.Constants.Companion.SLOT_ROAD
import com.future.pms.util.Constants.Companion.SLOT_SCAN_ME
import com.future.pms.util.Constants.Companion.SLOT_TAKEN
import com.future.pms.util.Constants.Companion.parkMargin
import com.future.pms.util.Constants.Companion.parkPadding
import com.future.pms.util.Constants.Companion.parkSize
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_parking_direction.*
import timber.log.Timber
import java.lang.ref.WeakReference
import java.util.*
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
  private lateinit var layoutPark: LinearLayout
  private lateinit var parkingLayout: LinearLayout
  private lateinit var asyncTask: SetupLayoutAsyc
  private var totalRow = 0
  private val LETTER = ArrayList(
      listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
          "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "A1", "A2", "A3", "A4", "A5", "A6", "A7",
          "A8", "A9", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "C1", "C2", "C3", "C4",
          "C5", "C6", "C7", "C8", "C9", "D1", "D2", "D3", "D4", "D5", "D6", "D7"))

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
    private val parkingDirectionFragment = activityReference.get()?.supportFragmentManager?.run {
      findFragmentByTag(TAG)
    }
    private val mParkingDirectionFragment = parkingDirectionFragment as ParkingDirectionFragment

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
      if (parkingDirectionFragment != null && activityReference.get() != null) {
        mParkingDirectionFragment.setSlotStatus(result[0])
      }
    }
  }

  fun setSlotStatus(result: String?) {
    if (result != "" && context != null) {
      val slotsLayout = result?.substring(result.length - 1)?.single()
      val index = result?.substring(0, result.length - 1)?.toInt()
      val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.WRAP_CONTENT)
      val numberingLeft = binding.numberingLeft as LinearLayout

      if (index != null) {

        if (index == 0 || index % SLOTS_IN_ROW == 0) {
          parkingLayout = LinearLayout(context)
          parkingLayout.orientation = LinearLayout.HORIZONTAL
          layoutPark.addView(parkingLayout)
          totalRow++

          val numbering = TextView(context)
          numbering.text = LETTER[totalRow - 1]
          params.weight = 1f
          numbering.layoutParams = params
          numbering.gravity = Gravity.CENTER
          numbering.setTextColor(resources.getColor(R.color.colorAccent))
          numberingLeft.addView(numbering)
        }

        when (slotsLayout) {
          SLOT_NULL -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_blank)
          }
          SLOT_SCAN_ME, SLOT_TAKEN -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_car)
          }
          SLOT_EMPTY -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_park)
          }
          DISABLED_SLOT -> {
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
          MY_SLOT -> {
            setupParkingView(index, parkingLayout, slotsLayout, R.drawable.ic_my_location)
          }
        }
        if (totalRow == 30) {
          showProgress(false)
        }
        if (totalRow == 31) {
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

  override fun onDestroy() {
    if (::asyncTask.isInitialized) {
      asyncTask.cancel(true)
    }
    presenter.detach()
    super.onDestroy()
  }
}
