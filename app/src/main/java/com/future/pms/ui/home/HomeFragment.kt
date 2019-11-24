package com.future.pms.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.future.pms.R
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
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class HomeFragment : Fragment(), HomeContract {
  @Inject lateinit var presenter: HomePresenter
  private lateinit var rootView: View

  fun newInstance(): HomeFragment {
    return HomeFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_home, container, false)
    val viewPager = rootView.viewPager as ViewPager
    val adapter = ViewPagerAdapter(childFragmentManager)
    adapter.addFragment(OngoingFragment(), "Your Ongoing Parking")
    adapter.addFragment(HistoryFragment(), "History")
    viewPager.adapter = adapter
    rootView.tabs.setupWithViewPager(viewPager)
    for (i in 0 until rootView.tabs.tabCount) {
      if (i == 0) rootView.tabs.getTabAt(i)?.setIcon(R.drawable.ic_parking)
      else rootView.tabs.getTabAt(i)?.setIcon(R.drawable.ic_history)
    }
    return rootView
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
            Constants.TOKEN, null), Token::class.java).access_token
    getDateNow()
    presenter.loadData(accessToken)
    val textAnnounce = rootView.findViewById(R.id.text_announce_user) as TextView
    textAnnounce.text = presenter.getTextAnnounce()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    presenter.unsubscribe()
  }

  override fun loadCustomerDetailSuccess(customer: Customer) {
    rootView.user_name.text = customer.body.name
  }

  override fun showErrorMessage(error: String) {
    Log.e(ERROR, error)
  }

  override fun unauthorized() {
    val activity = activity as MainActivity?
    activity?.presenter?.showLoginPage()
  }

  override fun getDateNow() {
    val currentDateTimeString = DateFormat.getDateInstance(DateFormat.FULL).format(Date())
    val dateText = rootView.findViewById(R.id.date_now) as TextView
    dateText.text = String.format("It's %s", currentDateTimeString)
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()
    homeComponent.inject(this)
  }

  companion object {
    const val TAG: String = HOME_FRAGMENT
  }
}