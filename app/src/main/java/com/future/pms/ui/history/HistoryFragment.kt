package com.future.pms.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.databinding.FragmentHistoryBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.history.BookingHistory
import com.future.pms.model.history.History
import com.future.pms.model.oauth.Token
import com.future.pms.ui.receipt.ReceiptFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HISTORY_FRAGMENT
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_history.*
import timber.log.Timber
import javax.inject.Inject

class HistoryFragment : Fragment(), HistoryContract {
  @Inject lateinit var presenter: HistoryPresenter
  private lateinit var binding: FragmentHistoryBinding
  private lateinit var historyAdapter: HistoryAdapter
  private var currentPage = 0
  private var isLastPage = false
  private lateinit var accessToken: String

  companion object {
    const val TAG = HISTORY_FRAGMENT
  }

  fun newInstance(): HistoryFragment {
    return HistoryFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    accessToken = Gson().fromJson(context?.getSharedPreferences(Constants.AUTHENTICATION,
            Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    binding.shimmerHistory.startShimmerAnimation()
    binding.refreshHistory.setOnRefreshListener {
      refreshListHistory()
    }
    historyAdapter = HistoryAdapter()
    binding.rvHistory.layoutManager = linearLayoutManager
    binding.rvHistory.adapter = this.historyAdapter
    historyAdapter.onItemClick = { history ->
      customerBookingClick(history)
    }
    binding.rvHistory.addOnScrollListener(object :
        PaginationScrollListener(linearLayoutManager, isLastPage) {
      override fun loadMoreItems() {
        if (!isLastPage) {
          presenter.loadCustomerBooking(accessToken, currentPage)
        }
      }
    })
    presenter.loadCustomerBooking(accessToken, currentPage)
    return binding.root
  }

  fun refreshListHistory() {
    shimmer_history.visibility = View.VISIBLE
    shimmer_history.startShimmerAnimation()
    historyAdapter.clear()
    historyAdapter.notifyDataSetChanged()
    currentPage = 0
    isLastPage = false
    presenter.loadCustomerBooking(accessToken, currentPage)
    binding.refreshHistory.isRefreshing = false
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
  }

  override fun onFailed(message: String) {
    Timber.tag(ERROR).e(message)
    isLastPage = true
  }

  override fun loadCustomerBookingSuccess(history: History) {
    shimmer_history.visibility = View.GONE
    shimmer_history.stopShimmerAnimation()
    if (currentPage != 0) {
      if (currentPage <= history.totalPages - 1) {
        historyAdapter.addAll(history.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      historyAdapter.addAll(history.content)
      if (history.content.isEmpty()) {
        binding.dontHaveOrder.visibility = View.VISIBLE
      }
      if (currentPage >= history.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
  }

  private fun customerBookingClick(history: BookingHistory) {
    val fragment = ReceiptFragment()
    val bundle = Bundle()
    bundle.putString(Constants.ID_BOOKING, history.idBooking)
    fragment.arguments = bundle
    activity?.supportFragmentManager?.let { fragmentManager ->
      if (!fragment.isAdded) {
        fragment.show(fragmentManager, fragment.tag)
      }
    }
  }

  override fun loadCustomerBookingError() {
    binding.dontHaveOrder.visibility = View.VISIBLE
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(
            FragmentModule(this)).build()

    homeComponent.inject(this)
  }
}