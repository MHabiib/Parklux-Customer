package com.future.pms.history.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentHistoryBinding
import com.future.pms.history.adapter.HistoryAdapter
import com.future.pms.history.injection.DaggerHistoryComponent
import com.future.pms.history.injection.HistoryComponent
import com.future.pms.history.model.BookingHistory
import com.future.pms.history.model.History
import com.future.pms.history.presenter.HistoryPresenter
import com.future.pms.receipt.view.ReceiptFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ERROR
import com.future.pms.util.Constants.Companion.HISTORY_FRAGMENT
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class HistoryFragment : BaseFragment(), HistoryContract {
  private var daggerBuild: HistoryComponent = DaggerHistoryComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: HistoryPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentHistoryBinding
  private lateinit var historyAdapter: HistoryAdapter
  private var currentPage = 0
  private var isLastPage = false
  private lateinit var accessToken: String

  companion object {
    const val TAG = HISTORY_FRAGMENT
  }

  fun newInstance(): HistoryFragment = HistoryFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    binding.shimmerHistory.startShimmer()
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
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken

    presenter.attach(this)
    presenter.subscribe()
    presenter.loadCustomerBooking(accessToken, currentPage)
  }

  override fun onFailed(message: String) {
    Timber.tag(ERROR).e(message)
    isLastPage = true
  }

  override fun loadCustomerBookingSuccess(history: History) {
    binding.shimmerHistory.visibility = View.GONE
    binding.shimmerHistory.stopShimmer()
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

  private fun refreshListHistory() {
    binding.dontHaveOrder.visibility = View.GONE
    binding.shimmerHistory.visibility = View.VISIBLE
    binding.shimmerHistory.startShimmer()
    historyAdapter.clear()
    historyAdapter.notifyDataSetChanged()
    currentPage = 0
    isLastPage = false
    presenter.loadCustomerBooking(accessToken, currentPage)
    binding.refreshHistory.isRefreshing = false
  }

  override fun loadCustomerBookingError() {
    binding.dontHaveOrder.visibility = View.VISIBLE
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}