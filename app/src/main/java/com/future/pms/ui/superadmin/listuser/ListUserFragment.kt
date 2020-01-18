package com.future.pms.ui.superadmin.listuser

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.pms.R
import com.future.pms.databinding.FragmentListUserBinding
import com.future.pms.di.component.DaggerFragmentComponent
import com.future.pms.di.module.FragmentModule
import com.future.pms.model.admin.Admin
import com.future.pms.model.admin.AdminDetails
import com.future.pms.model.admin.nonPage.AdminResponse
import com.future.pms.model.customer.CustomerDetails
import com.future.pms.model.customer.CustomerResponse
import com.future.pms.model.customerdetail.Customer
import com.future.pms.model.oauth.Token
import com.future.pms.model.superadmin.SuperAdminDetails
import com.future.pms.model.superadmin.SuperAdminResponse
import com.future.pms.model.user.UserDetails
import com.future.pms.ui.superadmin.userdetails.UserDetailsFragment
import com.future.pms.util.Constants
import com.future.pms.util.Constants.Companion.ID_USER
import com.future.pms.util.Constants.Companion.LIST_USER_FRAGMENT
import com.future.pms.util.Constants.Companion.ROLE
import com.future.pms.util.Constants.Companion.ROLE_ADMIN
import com.future.pms.util.Constants.Companion.ROLE_CUSTOMER
import com.future.pms.util.Constants.Companion.ROLE_SUPER_ADMIN
import com.future.pms.util.PaginationScrollListener
import com.google.gson.Gson
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ListUserFragment : Fragment(), ListUserContract {
  @Inject lateinit var presenter: ListUserPresenter
  private lateinit var binding: FragmentListUserBinding
  private lateinit var listCustomerAdapter: ListCustomerAdapter
  private lateinit var listSuperAdminAdapter: ListSuperAdminAdapter
  private lateinit var listAdminAdapter: ListAdminAdapter
  private var currentPage = 0
  private var isLastPage = false
  private var isLoading = false
  private var itemPosition: Int? = 0
  private lateinit var idItem: String
  private lateinit var accessToken: String
  private var searchByName = ""
  private val spinnerItems = ArrayList<String>()
  var handler = Handler(Looper.getMainLooper() /*UI thread*/)
  var workRunnable: Runnable? = null
  private val bottomSheetFragment = UserDetailsFragment()

  companion object {
    const val TAG: String = LIST_USER_FRAGMENT
  }

  fun newInstance(): ListUserFragment {
    return ListUserFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectDependency()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_user, container, false)
    presenter.attach(this)

    val linearLayoutManagerCustomer = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
        false)
    val linearLayoutManagerAdmin = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    val linearLayoutManagerSuperAdmin = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,
        false)

    with(binding) {
      shimmerUser.startShimmerAnimation()
      refreshCustomer.setOnRefreshListener {
        shimmerUser.startShimmerAnimation()
        shimmerUser.visibility = View.VISIBLE
        listCustomerAdapter.clear()
        listCustomerAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllCustomer(accessToken, currentPage, searchByName)
        refreshCustomer.isRefreshing = false
      }
      refreshAdmin.setOnRefreshListener {
        shimmerUser.startShimmerAnimation()
        shimmerUser.visibility = View.VISIBLE
        listAdminAdapter.clear()
        listAdminAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllAdmin(accessToken, currentPage, searchByName)
        refreshAdmin.isRefreshing = false
      }
      refreshSuperAdmin.setOnRefreshListener {
        shimmerUser.startShimmerAnimation()
        shimmerUser.visibility = View.VISIBLE
        listSuperAdminAdapter.clear()
        listSuperAdminAdapter.notifyDataSetChanged()
        currentPage = 0
        isLastPage = false
        presenter.loadAllSuperAdmin(accessToken, currentPage)
        refreshSuperAdmin.isRefreshing = false
      }
      listCustomerAdapter = ListCustomerAdapter()
      listAdminAdapter = ListAdminAdapter()
      listSuperAdminAdapter = ListSuperAdminAdapter()

      rvCustomer.layoutManager = linearLayoutManagerCustomer
      rvAdmin.layoutManager = linearLayoutManagerAdmin
      rvSuperAdmin.layoutManager = linearLayoutManagerSuperAdmin
      rvCustomer.adapter = listCustomerAdapter
      rvAdmin.adapter = listAdminAdapter
      rvSuperAdmin.adapter = listSuperAdminAdapter

      listCustomerAdapter.onItemClick = {
        itemPosition = it.position
        idItem = it.idCustomer
        val bundle = Bundle()
        bundle.putString(ROLE, ROLE_CUSTOMER)
        bundle.putString(ID_USER, it.idCustomer)
        bottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { fragmentManager ->
          if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
          }
        }
      }
      listAdminAdapter.onItemClick = {
        itemPosition = it.position
        idItem = it.idParkingZone
        val bundle = Bundle()
        bundle.putString(ROLE, ROLE_ADMIN)
        bundle.putString(ID_USER, it.idParkingZone)
        bottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { fragmentManager ->
          if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
          }
        }
      }
      listSuperAdminAdapter.onItemClick = {
        itemPosition = it.position
        idItem = it.idUser
        val bundle = Bundle()
        bundle.putString(ROLE, ROLE_SUPER_ADMIN)
        bundle.putString(ID_USER, it.idUser)
        bottomSheetFragment.arguments = bundle
        activity?.supportFragmentManager?.let { fragmentManager ->
          if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
          }
        }
      }

      rvCustomer.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManagerCustomer, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllCustomer(accessToken, currentPage, searchByName)
          }
        }
      })
      rvAdmin.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManagerAdmin, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllAdmin(accessToken, currentPage, searchByName)
          }
        }
      })
      rvSuperAdmin.addOnScrollListener(object :
          PaginationScrollListener(linearLayoutManagerSuperAdmin, isLastPage) {
        override fun loadMoreItems() {
          if (!isLoading && !isLastPage) {
            isLoading = true
            presenter.loadAllSuperAdmin(accessToken, currentPage)
          }
        }
      })

      val adapter = context?.let { ArrayAdapter(it, R.layout.spinner_style, spinnerItems) }
      adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      spinnerItems.add(getString(R.string.customer))
      spinnerItems.add(getString(R.string.admin))
      spinnerItems.add(getString(R.string.other_super_admin))
      name.adapter = adapter
      name.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
          //No implementation needed
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
          (p0?.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.white))
          shimmerUser.startShimmerAnimation()
          shimmerUser.visibility = View.VISIBLE
          when (p2) {
            0 -> {
              onSpinnerChange()
              refreshAdmin.visibility = View.GONE
              refreshSuperAdmin.visibility = View.GONE
              refreshCustomer.visibility = View.VISIBLE
              presenter.loadAllCustomer(accessToken, currentPage, searchByName)
              refreshCustomer.isRefreshing = false
              searchName.visibility = View.VISIBLE
            }
            1 -> {
              onSpinnerChange()
              refreshCustomer.visibility = View.GONE
              refreshSuperAdmin.visibility = View.GONE
              refreshAdmin.visibility = View.VISIBLE
              presenter.loadAllAdmin(accessToken, currentPage, searchByName)
              refreshAdmin.isRefreshing = false
              searchName.visibility = View.VISIBLE
            }
            else -> {
              onSpinnerChange()
              refreshCustomer.visibility = View.GONE
              refreshAdmin.visibility = View.GONE
              refreshSuperAdmin.visibility = View.VISIBLE
              presenter.loadAllSuperAdmin(accessToken, currentPage)
              refreshSuperAdmin.isRefreshing = false
              searchName.visibility = View.GONE
            }
          }
        }
      }
      inputSearchName.addTextChangedListener {
        handler.removeCallbacks(workRunnable)
        searchByName = inputSearchName.text.toString()
        workRunnable = Runnable {
          when {
            name.selectedItem == getString(R.string.customer) -> {
              shimmerUser.startShimmerAnimation()
              shimmerUser.visibility = View.VISIBLE
              listCustomerAdapter.clear()
              listCustomerAdapter.notifyDataSetChanged()
              currentPage = 0
              isLastPage = false
              presenter.loadAllCustomer(accessToken, currentPage, searchByName)
              refreshCustomer.isRefreshing = false
            }
            name.selectedItem == getString(R.string.admin) -> {
              shimmerUser.startShimmerAnimation()
              shimmerUser.visibility = View.VISIBLE
              listAdminAdapter.clear()
              listAdminAdapter.notifyDataSetChanged()
              currentPage = 0
              isLastPage = false
              presenter.loadAllAdmin(accessToken, currentPage, searchByName)
              refreshAdmin.isRefreshing = false
            }
          }
        }
        handler.postDelayed(workRunnable, 500 /*delay*/)
      }
      return root
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    accessToken = Gson().fromJson(
        context?.getSharedPreferences(Constants.AUTHENTCATION, Context.MODE_PRIVATE)?.getString(
            Constants.TOKEN, null), Token::class.java).accessToken
    presenter.apply {
      subscribe()
    }
  }

  override fun loadAllCustomerSuccess(customer: CustomerResponse) {
    binding.shimmerUser.stopShimmerAnimation()
    binding.shimmerUser.visibility = View.GONE
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
        //todo dont have parking zone
      }
      if (currentPage >= customer.body.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
    isLoading = false
  }

  override fun loadAllAdminSuccess(admin: Admin) {
    binding.shimmerUser.stopShimmerAnimation()
    binding.shimmerUser.visibility = View.GONE
    if (currentPage != 0) {
      if (currentPage <= admin.totalPages - 1) {
        listAdminAdapter.addAll(admin.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listAdminAdapter.addAll(admin.content)
      if (admin.content.isEmpty()) {
        //todo dont have parking zone
      }
      if (currentPage >= admin.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
    isLoading = false
  }

  override fun loadAllSuperAdminSuccess(superAdmin: SuperAdminResponse) {
    binding.shimmerUser.stopShimmerAnimation()
    binding.shimmerUser.visibility = View.GONE
    if (currentPage != 0) {
      if (currentPage <= superAdmin.body.totalPages - 1) {
        listSuperAdminAdapter.addAll(superAdmin.body.content)
        currentPage += 1
      } else {
        isLastPage = true
      }
    } else {
      listSuperAdminAdapter.addAll(superAdmin.body.content)
      if (superAdmin.body.content.isEmpty()) {
        //todo dont have parking zone
      }
      if (currentPage >= superAdmin.body.totalPages - 1) {
        isLastPage = true
      } else {
        currentPage += 1
      }
    }
    isLoading = false
  }

  fun updateCustomerSuccess(customer: Customer) {
    bottomSheetFragment.dismiss()
    itemPosition?.let {
      listCustomerAdapter.remove(it)
      customer.body.let { customerDetails ->
        val details = CustomerDetails(name = customerDetails.name, email = customerDetails.email,
            idCustomer = customerDetails.idCustomer, phoneNumber = customerDetails.phoneNumber,
            position = 0)
        listCustomerAdapter.addAt(it, details)
      }
      listCustomerAdapter.notifyItemChanged(it)
    }
  }

  fun updateAdminSuccess(adminResponse: AdminResponse) {
    bottomSheetFragment.dismiss()
    itemPosition?.let {
      listAdminAdapter.remove(it)
      adminResponse.body.let { adminDetails ->
        val details = AdminDetails(address = adminDetails.address,
            emailAdmin = adminDetails.emailAdmin, idParkingZone = adminDetails.idParkingZone,
            imageUrl = adminDetails.imageUrl, name = adminDetails.name,
            openHour = adminDetails.openHour, phoneNumber = adminDetails.phoneNumber,
            price = adminDetails.price, position = 0)
        listAdminAdapter.addAt(it, details)
      }
      listAdminAdapter.notifyItemChanged(it)
    }
  }

  fun updateSuperAdminSuccess(user: UserDetails) {
    bottomSheetFragment.dismiss()
    itemPosition?.let {
      listSuperAdminAdapter.remove(it)
      val superAdmin = user.idUser?.let { idUser ->
        SuperAdminDetails(user.email, idUser, ROLE_SUPER_ADMIN, 0)
      }
      superAdmin?.let { superAdminDetails -> listSuperAdminAdapter.addAt(it, superAdminDetails) }
      listSuperAdminAdapter.notifyItemChanged(it)
    }
  }

  fun deleteSuperAdminSuccess() {
    bottomSheetFragment.dismiss()
    itemPosition?.let {
      listSuperAdminAdapter.remove(it)
      listSuperAdminAdapter.notifyItemChanged(it)
    }
  }

  private fun onSpinnerChange() {
    listCustomerAdapter.clear()
    listCustomerAdapter.notifyDataSetChanged()
    listAdminAdapter.clear()
    listAdminAdapter.notifyDataSetChanged()
    listSuperAdminAdapter.clear()
    listSuperAdminAdapter.notifyDataSetChanged()
    currentPage = 0
    isLastPage = false
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