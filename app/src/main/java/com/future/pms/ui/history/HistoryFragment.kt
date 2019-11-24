package com.future.pms.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.model.oauth.Token
import com.future.pms.ui.main.MainActivity
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Utils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import javax.inject.Inject

class HistoryFragment : Fragment(), HistoryContract {
  @Inject lateinit var presenter: HistoryPresenter
  private lateinit var rootView: View

  fun newInstance(): HistoryFragment {
    return HistoryFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    rootView = inflater.inflate(R.layout.fragment_history, container, false)
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
    presenter.loadCustomerBooking(accessToken)
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

  override fun showErrorMessage(error: String) {
    Log.e(ERROR, error)
  }

  override fun loadCustomerBookingSuccess(list: List<CustomerBooking>) {
    if (list.isEmpty() || list.size == 1 && 0L == list[0].dateOut) rootView.dont_have_order.visibility = View.VISIBLE
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.setHasFixedSize(true)
    recyclerView.adapter = HistoryAdapter(
        Utils.getHistoryParking(list.reversed())) { booking: CustomerBooking ->
      customerBookingClick(booking)
    }
  }

  private fun customerBookingClick(booking: CustomerBooking) {
    val activity = activity as MainActivity?
    activity?.presenter?.showReceipt(booking.idBooking)
  }

  override fun loadCustomerBookingError() {
    rootView.dont_have_order.visibility = View.VISIBLE
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()

    homeComponent.inject(this)
  }
}