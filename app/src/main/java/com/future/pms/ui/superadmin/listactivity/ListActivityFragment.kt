package com.future.pms.ui.superadmin.listactivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.model.activity.Booking
import com.future.pms.databinding.FragmentListActivityBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.LIST_ACTIVITY_FRAGMENT
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class ListActivityFragment : Fragment(), ListActivityContract {
  @Inject lateinit var presenter: ListActivityPresenter
  private lateinit var binding: FragmentListActivityBinding
  private lateinit var listActivityAdapter: ListActivityAdapter
  private var currentPage = 0
  private var isLastPage = false
  private var isLoading = false
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = LIST_ACTIVITY_FRAGMENT
  }

  fun newInstance(): ListActivityFragment {
    return ListActivityFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_activity, container, false)
    presenter.attach(this)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    with(binding) {
      refreshActivity.setOnRefreshListener {
        listActivityAdapter.clear()
        listActivityAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllBooking(accessToken, currentPage)
        refreshActivity.isRefreshing = false
      }
      listActivityAdapter = ListActivityAdapter()
      rvActivity.layoutManager = linearLayoutManager
      rvActivity.adapter = listActivityAdapter
      listActivityAdapter.onItemClick = { history ->
        Toast.makeText(context, "A", Toast.LENGTH_LONG).show()
      }
      rvActivity.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManager, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllBooking(accessToken, currentPage)
          }
        }
      })
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      loadAllBooking(accessToken, currentPage)
      subscribe()
    }
  }

  override fun loadAllBookingSuccess(booking: Booking) {
    if (currentPage != 0) {
      if (currentPage <= booking.totalPages - 1) {
        listActivityAdapter.addAll(booking.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listActivityAdapter.addAll(booking.content)
      if (booking.content.isEmpty()) {
        //gaada parkingzone
      }
      if (currentPage >= booking.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
    isLoading = false
  }

  override fun onFailed(e: String) {
    Timber.e(e)
  }

  private fun injectDependency() {
    val profileComponent = DaggerFragmentComponent.builder().fragmentModule(
        FragmentModule()).build()
    profileComponent.inject(this)
  }
}