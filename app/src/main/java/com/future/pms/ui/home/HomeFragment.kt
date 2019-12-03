package com.future.pms.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.future.pms.R
import com.future.pms.databinding.FragmentHomeBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.ui.history.HistoryFragment
import com.future.pms.ui.main.MainActivity
import com.future.pms.ui.ongoing.OngoingFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HOME_FRAGMENT
import com.google.gson.Gson
import timber.log.Timber
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {
  @Inject lateinit var presenter: HomePresenter
  private lateinit var binding: FragmentHomeBinding

  companion object {
    const val TAG: String = HOME_FRAGMENT
  }

  fun newInstance(): HomeFragment {
    return HomeFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    with(binding) {
      val viewPager = viewPager as ViewPager
      val adapter = ViewPagerAdapter(childFragmentManager)
      adapter.addFragment(OngoingFragment(), "Your Ongoing Parking")
      adapter.addFragment(HistoryFragment(), "History")
      viewPager.adapter = adapter
      tabs.setupWithViewPager(viewPager)
      for (i in 0 until tabs.tabCount) {
        if (i == 0) tabs.getTabAt(i)?.setIcon(R.drawable.ic_parking)
        else tabs.getTabAt(i)?.setIcon(R.drawable.ic_history)
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
    initView()
  }

  private fun initView() {
    val accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
          Constants.TOKEN, null
        ), Token::class.java
    ).accessToken
    getDateNow()
    presenter.loadData(accessToken)
    val textAnnounce = binding.textAnnounceUser
    textAnnounce.text = presenter.getTextAnnounce()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    presenter.unsubscribe()
  }

  override fun loadCustomerDetailSuccess(customer: Customer) {
    binding.userName.text = customer.body.name
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(ERROR).e(error)
  }

  override fun unauthorized() {
    val activity = activity as MainActivity?
    activity?.presenter?.showLoginPage()
  }

  override fun getDateNow() {
    val currentDateTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(Date())
    val dateText = binding.dateNow
    dateText.text = String.format(getString(R.string.date_now), currentDateTimeString)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }
}