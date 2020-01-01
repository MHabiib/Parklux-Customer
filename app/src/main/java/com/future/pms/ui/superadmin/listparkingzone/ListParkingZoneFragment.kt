package com.future.pms.ui.superadmin.listparkingzone

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
import com.future.pms.databinding.FragmentListParkingZoneBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.oauth.Token
import com.future.pms.model.parkingzone.ParkingZone
import com.future.pms.util.Constants.Companion.AUTHENTCATION
import com.future.pms.util.Constants.Companion.LIST_PARKING_ZONE_FRAGMENT
import com.future.pms.util.Constants.Companion.TOKEN
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class ListParkingZoneFragment : Fragment(), ListParkingZoneContract {
  @Inject lateinit var presenter: ListParkingZonePresenter
  private lateinit var binding: FragmentListParkingZoneBinding
  private lateinit var listParkingZoneAdapter: ListParkingZoneAdapter
  private var currentPage = 0
  private var isLastPage = false
  private var isLoading = false
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = LIST_PARKING_ZONE_FRAGMENT
  }

  fun newInstance(): ListParkingZoneFragment {
    return ListParkingZoneFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_parking_zone, container,
        false)
    presenter.attach(this)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    with(binding) {
      refreshParkingZone.setOnRefreshListener {
        listParkingZoneAdapter.clear()
        listParkingZoneAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllParkingZone(accessToken, currentPage)
        refreshParkingZone.isRefreshing = false
      }
      listParkingZoneAdapter = ListParkingZoneAdapter()
      rvParkingZone.layoutManager = linearLayoutManager
      rvParkingZone.adapter = listParkingZoneAdapter
      listParkingZoneAdapter.onItemClick = { history ->
        Toast.makeText(context, "A", Toast.LENGTH_LONG).show()
      }
      rvParkingZone.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManager, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllParkingZone(accessToken, currentPage)
          }
        }
      })
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(AUTHENTCATION, Context.MODE_PRIVATE)?.getString(TOKEN, null),
        Token::class.java).accessToken
    presenter.apply {
      loadAllParkingZone(accessToken, currentPage)
      subscribe()
    }
  }

  override fun loadAllParkingZoneSuccess(parkingZone: ParkingZone) {
    if (currentPage != 0) {
      if (currentPage <= parkingZone.totalPages - 1) {
        listParkingZoneAdapter.addAll(parkingZone.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listParkingZoneAdapter.addAll(parkingZone.content)
      if (parkingZone.content.isEmpty()) {
        //gaada parkingzone
      }
      if (currentPage >= parkingZone.totalPages - 1) {
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