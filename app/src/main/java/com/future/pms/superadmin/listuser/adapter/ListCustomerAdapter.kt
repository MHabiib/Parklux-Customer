package com.future.pms.superadmin.listuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.superadmin.listuser.model.customer.CustomerDetails
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
    return HistoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_layout_customer, parent, false))
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val customer = customerList?.get(position)
    if (getItemViewType(position) == item) {
      val historyViewHolder = holder as HistoryViewHolder
      historyViewHolder.itemTitle.text = customer?.name
      historyViewHolder.itemEmail.text = customer?.email
      historyViewHolder.itemPhone.text = customer?.phoneNumber
      customer?.name?.let {
        if (!it.contains(" (BANNED)")) {
          historyViewHolder.itemLayout.setBackgroundResource(R.drawable.card_layout_dark_dark_grey)
        } else {
          historyViewHolder.itemLayout.setBackgroundResource(R.drawable.card_layout_dark_red)
        }
      }
    }
  }

  override fun getItemCount(): Int = customerList?.size ?: 0

  override fun getItemViewType(position: Int): Int = if (position == customerList?.size?.minus(
          1) && isLoadingAdded) loading else item

  fun add(customer: CustomerDetails) {
    customerList?.add(customer)
    customerList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(customerResult: List<CustomerDetails>) {
    for (result in customerResult) {
      add(result)
    }
  }

  fun addAt(position: Int, customer: CustomerDetails) = customerList?.add(position, customer)

  fun remove(position: Int) {
    customerList?.removeAt(position)
    notifyItemRemoved(position)
    customerList?.size?.let { notifyItemRangeChanged(position, it) }
  }

  fun clear() = customerList?.clear()

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemEmail: TextView = itemView.findViewById<View>(R.id.item_email) as TextView
    val itemPhone: TextView = itemView.findViewById<View>(R.id.item_phone) as TextView
    val itemLayout: ConstraintLayout = itemView.findViewById<View>(
        R.id.item_layout) as ConstraintLayout

    init {
      itemView.setOnClickListener {
        customerList?.get(adapterPosition)?.let { customerDetails ->
          customerDetails.position = adapterPosition
          onItemClick?.invoke(customerDetails)
        }
      }
    }
  }
}
