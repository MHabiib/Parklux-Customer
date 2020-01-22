package com.future.pms.ui.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.databinding.FragmentHomeBinding
import com.future.pms.model.customerdetail.Body
import com.future.pms.model.oauth.Token
import com.future.pms.ui.base.BaseFragment
import com.future.pms.ui.history.HistoryFragment
import com.future.pms.ui.home.DaggerHomeComponent
import com.future.pms.ui.home.injection.HomeComponent
import com.future.pms.ui.home.presenter.HomePresenter
import com.future.pms.ui.ongoing.OngoingFragment
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
  private lateinit var binding: FragmentHomeBinding

  companion object {
    const val TAG: String = HOME_FRAGMENT
  }

  fun newInstance(): HomeFragment {
    return HomeFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.onOngoingIconClick()
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
    initView()
  }

  private fun initView() {
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    getDateNow()
    presenter.loadData(accessToken)
    val textAnnounce = binding.textAnnounceUser
    textAnnounce.text = presenter.getTextAnnounce()
  }

  override fun showOngoingFragment() {
    activity?.let { it ->
      if (it.supportFragmentManager.findFragmentByTag(OngoingFragment.TAG) == null) {
        it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
            R.animator.fade_out).add(R.id.frame_home, OngoingFragment().newInstance(),
            OngoingFragment.TAG).commit()
      } else {
        it.supportFragmentManager.run { findFragmentByTag(OngoingFragment.TAG) }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).show(fragment).commit()
        }
      }
      if (it.supportFragmentManager.findFragmentByTag(HistoryFragment.TAG) != null) {
        it.supportFragmentManager.run { findFragmentByTag(HistoryFragment.TAG) }?.let { fragment ->
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
        it.supportFragmentManager.run { findFragmentByTag(HistoryFragment.TAG) }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).show(fragment).commit()
        }
      }
      if (it.supportFragmentManager.findFragmentByTag(OngoingFragment.TAG) != null) {
        it.supportFragmentManager.run { findFragmentByTag(OngoingFragment.TAG) }?.let { fragment ->
          it.supportFragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in,
              R.animator.fade_out).hide(fragment).commit()
        }
      }
    }
  }

  override fun loadCustomerDetailSuccess(customer: Body) {
    binding.userName.text = customer.name
  }

  override fun onFailed(message: String) {
    Timber.tag(ERROR).e(message)
  }

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