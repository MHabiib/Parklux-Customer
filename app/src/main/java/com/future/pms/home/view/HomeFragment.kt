package com.future.pms.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.Token
import com.future.pms.core.model.customerdetails.Body
import com.future.pms.databinding.FragmentHomeBinding
import com.future.pms.history.view.HistoryFragment
import com.future.pms.home.injection.DaggerHomeComponent
import com.future.pms.home.injection.HomeComponent
import com.future.pms.home.presenter.HomePresenter
import com.future.pms.ongoing.view.OngoingFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT
import com.google.gson.Gson
import timber.log.Timber
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), HomeContract {
  private var daggerBuild: HomeComponent = DaggerHomeComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: HomePresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentHomeBinding

  companion object {
    const val TAG: String = HOME_FRAGMENT
  }

  fun newInstance(): HomeFragment = HomeFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    with(binding) {
      ongoing.setOnClickListener {
        presenter.onOngoingIconClick()
        ongoingIndicator.visibility = View.VISIBLE
        historyIndicator.visibility = View.GONE
      }
      history.setOnClickListener {
        presenter.onHistoryIconClick()
        ongoingIndicator.visibility = View.GONE
        historyIndicator.visibility = View.VISIBLE
      }
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.onOngoingIconClick()
    val accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    getDateNow()
    presenter.loadData(accessToken)

    val textAnnounce = binding.textAnnounceUser
    textAnnounce.text = getTextAnnounce()
  }

  private fun getTextAnnounce(): String {
    return when (Calendar.getInstance(TimeZone.getTimeZone(getString(R.string.asiajakarta))).get(
        Calendar.HOUR_OF_DAY)) {
      in 0 .. 11 -> getString(R.string.good_morning)
      in 12 .. 15 -> getString(R.string.good_afternoon)
      in 16 .. 20 -> getString(R.string.good_evening)
      in 21 .. 23 -> getString(R.string.good_night)
      else -> {
        getString(R.string.hello)
      }
    }
  }

  override fun showOngoingFragment() {
    activity?.let { it ->
      if (it.supportFragmentManager.findFragmentByTag(OngoingFragment.TAG) == null) {
        it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).add(R.id.frame_home, OngoingFragment().newInstance(),
            OngoingFragment.TAG).commit()
      } else {
        it.supportFragmentManager.run {
          findFragmentByTag(OngoingFragment.TAG)
        }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).show(fragment).commit()
        }
      }
      if (it.supportFragmentManager.findFragmentByTag(HistoryFragment.TAG) != null) {
        it.supportFragmentManager.run {
          findFragmentByTag(HistoryFragment.TAG)
        }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).hide(fragment).commit()
        }
      }
    }
  }

  override fun showHistoryFragment() {
    activity?.let {
      if (it.supportFragmentManager.findFragmentByTag(HistoryFragment.TAG) == null) {
        it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).add(R.id.frame_home, HistoryFragment().newInstance(),
            HistoryFragment.TAG).commit()
      } else {
        it.supportFragmentManager.run {
          findFragmentByTag(HistoryFragment.TAG)
        }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).show(fragment).commit()
        }
      }
      if (it.supportFragmentManager.findFragmentByTag(OngoingFragment.TAG) != null) {
        it.supportFragmentManager.run {
          findFragmentByTag(OngoingFragment.TAG)
        }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).hide(fragment).commit()
        }
      }
    }
  }

  override fun loadCustomerDetailSuccess(customer: Body) {
    binding.userName.text = customer.name
  }

  override fun onFailed(message: String) = Timber.tag(ERROR).e(message)

  override fun getDateNow() {
    val currentDateTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(Date())
    val dateText = binding.dateNow
    dateText.text = String.format(getString(R.string.date_now), currentDateTimeString)
  }

  override fun onDestroyView() {
    presenter.detach()
    super.onDestroyView()
  }
}