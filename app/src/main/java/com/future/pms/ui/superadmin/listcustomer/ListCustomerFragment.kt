package com.future.pms.ui.superadmin.listcustomer

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
import com.future.pms.databinding.FragmentListCustomerBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.oauth.Token
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.LIST_CUSTOMER_FRAGMENT
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class ListCustomerFragment : Fragment(), ListCustomerContract {
  @Inject lateinit var presenter: ListCustomerPresenter
  private lateinit var binding: FragmentListCustomerBinding
  private lateinit var listCustomerAdapter: ListCustomerAdapter
  private var currentPage = 0
  private var isLastPage = false
  private var isLoading = false
  private lateinit var accessToken: String

  companion object {
    const val TAG: String = LIST_CUSTOMER_FRAGMENT
  }

  fun newInstance(): ListCustomerFragment {
    return ListCustomerFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_customer, container, false)
    presenter.attach(this)
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    with(binding) {
      refreshCustomer.setOnRefreshListener {
        listCustomerAdapter.clear()
        listCustomerAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllCustomer(accessToken, currentPage)
        refreshCustomer.isRefreshing = false
      }
      listCustomerAdapter = ListCustomerAdapter()
      rvCustomer.layoutManager = linearLayoutManager
      rvCustomer.adapter = listCustomerAdapter
      listCustomerAdapter.onItemClick = { history ->
        Toast.makeText(context, "A", Toast.LENGTH_LONG).show()
      }
      rvCustomer.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManager, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllCustomer(accessToken, currentPage)
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
      loadAllCustomer(accessToken, currentPage)
      subscribe()
    }
  }

  override fun loadAllCustomerSuccess(customer: CustomerResponse) {
    if (currentPage != 0) {
      if (currentPage <= customer.body.totalPages - 1) {
        listCustomerAdapter.addAll(customer.body.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listCustomerAdapter.addAll(customer.body.content)
      if (customer.body.content.isEmpty()) {
        //gaada parkingzone
      }
      if (currentPage >= customer.body.totalPages - 1) {
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