package com.future.pms.ui.history

import android.content.Context
import android.os.Build
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
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    binding.refreshHistory.setOnRefreshListener {
      historyAdapter.clear()
      historyAdapter.notifyDataSetChanged()
      currentPage = 0
      isLastPage = false
      presenter.loadCustomerBooking(accessToken, currentPage)
      binding.refreshHistory.isRefreshing = false
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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.attach(this)
    presenter.subscribe()
  }

  override fun showErrorMessage(error: String) {
    Timber.tag(ERROR).e(error)
    isLastPage = true
  }

  override fun loadCustomerBookingSuccess(history: History) {
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
    activity?.supportFragmentManager?.let { it1 ->
      if (!fragment.isAdded) {
        fragment.show(it1, fragment.tag)
      }
    }
  }

  override fun loadCustomerBookingError() {
    binding.dontHaveOrder.visibility = View.VISIBLE
  }

  fun refreshPage() {
    val ft = fragmentManager?.beginTransaction()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      ft?.setReorderingAllowed(false)
    }
    ft?.detach(this)?.attach(this)?.commit()
  }

  private fun injectDependency() {
    val homeComponent = DaggerFragmentComponent.builder().fragmentModule(FragmentModule()).build()

    homeComponent.inject(this)
  }
}