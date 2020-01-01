package com.future.pms.ui.superadmin.listcustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.model.customer.CustomerDetails
import java.util.*

class ListCustomerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((CustomerDetails) -> Unit)? = null
  private var customerList: MutableList<CustomerDetails>? = null
  private lateinit var publicParent: ViewGroup
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    customerList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    publicParent = parent
    val inflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      item -> {
        val viewItem = inflater.inflate(R.layout.item_layout_customer, parent, false)
        HistoryViewHolder(viewItem)
      }
      else -> {
        val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
        LoadingViewHolder(viewLoading)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val customer = customerList?.get(position)
    when (getItemViewType(position)) {
      item -> {
        val historyViewHolder = holder as HistoryViewHolder
        historyViewHolder.itemTitle.text = customer?.name
        historyViewHolder.itemEmail.text = customer?.email
        historyViewHolder.itemPhone.text = customer?.phoneNumber
      }

      loading -> {
        val loadingViewHolder = holder as LoadingViewHolder
        loadingViewHolder.progressBar.visibility = View.VISIBLE
      }
    }
  }

  override fun getItemCount(): Int {
    return customerList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == customerList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(customer: CustomerDetails) {
    customerList?.add(customer)
    customerList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(customerResult: List<CustomerDetails>) {
    for (result in customerResult) {
      add(result)
    }
  }

  fun clear() {
    customerList?.clear()
  }

  private fun getItem(position: Int): CustomerDetails? {
    return customerList?.get(position)
  }

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemEmail: TextView = itemView.findViewById<View>(R.id.item_email) as TextView
    val itemPhone: TextView = itemView.findViewById<View>(R.id.item_phone) as TextView

    init {
      itemView.setOnClickListener {
        customerList?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
      }
    }
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById<View>(
        R.id.loadmore_progress) as ProgressBar
  }

}
