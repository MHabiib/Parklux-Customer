package com.future.pms.superadmin.listactivity.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.BaseApp
import com.future.pms.R
import com.future.pms.core.base.BaseFragment
import com.future.pms.core.model.Token
import com.future.pms.databinding.FragmentListActivityBinding
import com.future.pms.superadmin.activitydetails.model.Booking
import com.future.pms.superadmin.activitydetails.model.Content
import com.future.pms.superadmin.activitydetails.view.ActivityDetailsFragment
import com.future.pms.superadmin.listactivity.adapter.ListActivityAdapter
import com.future.pms.superadmin.listactivity.injection.DaggerListActivityComponent
import com.future.pms.superadmin.listactivity.injection.ListActivityComponent
import com.future.pms.superadmin.listactivity.presenter.ListActivityPresenter
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ALL
import com.future.pms.util.Constants.Companion.ID_BOOKING
import com.future.pms.util.Constants.Companion.LIST_ACTIVITY_FRAGMENT
import com.future.pms.util.Constants.Companion.ONGOING
import com.future.pms.util.Constants.Companion.PAST
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ListActivityFragment : BaseFragment(), ListActivityContract {
  private var daggerBuild: ListActivityComponent = DaggerListActivityComponent.builder().baseComponent(
      BaseApp.instance.baseComponent).build()

  init {
    daggerBuild.inject(this)
  }

  @Inject lateinit var presenter: ListActivityPresenter
  @Inject lateinit var gson: Gson
  private lateinit var binding: FragmentListActivityBinding
  private lateinit var listActivityAdapter: ListActivityAdapter
  private var currentPage = 0
  private var isLastPage = false
  private var isLoading = false
  private var itemPosition: Int? = 0
  private lateinit var idItem: String
  private lateinit var accessToken: String
  private lateinit var filterBy: String
  private val spinnerItems = ArrayList<String>()
  private val bottomSheetFragment = ActivityDetailsFragment()

  companion object {
    const val TAG: String = LIST_ACTIVITY_FRAGMENT
  }

  fun newInstance(): ListActivityFragment = ListActivityFragment()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_activity, container, false)
    presenter.attach(this)

    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    with(binding) {
      shimmerActivity.startShimmer()
      refreshActivity.setOnRefreshListener {
        refreshList()
        presenter.loadAllBooking(accessToken, currentPage, filterBy)
        refreshActivity.isRefreshing = false
        refreshActivity.isEnabled = false
      }
      listActivityAdapter = ListActivityAdapter()
      rvActivity.layoutManager = linearLayoutManager
      rvActivity.adapter = listActivityAdapter
      listActivityAdapter.onItemClick = {
        itemPosition = it.position
        idItem = it.idBooking.toString()
        val bundle = Bundle()
        bundle.putString(ID_BOOKING, it.idBooking)
        bottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { fragmentManager ->
          if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
          }
        }
      }
      rvActivity.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManager, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllBooking(accessToken, currentPage, filterBy)
          }
        }
      })

      val adapter = context?.let {
        ArrayAdapter(it, R.layout.spinner_style_small_text, spinnerItems)
      }
      adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      spinnerItems.add(getString(R.string.all))
      spinnerItems.add(getString(R.string.ongoing))
      spinnerItems.add(getString(R.string.completed))
      filter.adapter = adapter
      filter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
          //No implementation needed
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          (p0?.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.white))
          when (p2) {
            0 -> {
              filterBy = ALL
              refreshList()
              presenter.loadAllBooking(accessToken, currentPage, ALL)
              refreshActivity.isRefreshing = false
              refreshActivity.isEnabled = false
            }
            1 -> {
              filterBy = ONGOING
              refreshList()
              presenter.loadAllBooking(accessToken, currentPage, ONGOING)
              refreshActivity.isRefreshing = false
              refreshActivity.isEnabled = false
            }
            else -> {
              filterBy = PAST
              refreshList()
              presenter.loadAllBooking(accessToken, currentPage, PAST)
              refreshActivity.isRefreshing = false
              refreshActivity.isEnabled = false
            }
          }
        }
      }
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = gson.fromJson(
        context?.getSharedPreferences(Constants.AUTHENTICATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      subscribe()
    }
  }

  override fun loadAllBookingSuccess(booking: Booking) {
    binding.shimmerActivity.stopShimmer()
    binding.shimmerActivity.visibility = View.GONE
    binding.refreshActivity.isEnabled = true
    if (currentPage != 0) {
      if (currentPage <= booking.totalPages - 1) {
        listActivityAdapter.addAll(booking.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listActivityAdapter.addAll(booking.content)
      if (currentPage >= booking.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
    isLoading = false
  }

  private fun FragmentListActivityBinding.refreshList() {
    shimmerActivity.startShimmer()
    binding.shimmerActivity.visibility = View.VISIBLE
    listActivityAdapter.clear()
    listActivityAdapter.notifyDataSetChanged()
    currentPage = 0
    isLastPage = false
  }

  fun updatedList() {
    bottomSheetFragment.dismiss()
    presenter.findBookingById(idItem, accessToken)
  }

  override fun findBookingByIdSuccess(booking: Content) {
    itemPosition?.let {
      listActivityAdapter.remove(it)
      listActivityAdapter.addAt(it, booking)
      listActivityAdapter.notifyItemChanged(it)
    }
  }

  override fun onFailed(message: String) {
    Timber.e(message)
    binding.refreshActivity.isEnabled = true
  }

  override fun onDestroy() {
    presenter.detach()
    super.onDestroy()
  }
}