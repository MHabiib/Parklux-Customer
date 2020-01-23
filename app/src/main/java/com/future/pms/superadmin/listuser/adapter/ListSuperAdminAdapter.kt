package com.future.pms.superadmin.listuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.superadmin.listuser.model.superadmin.SuperAdminDetails
import java.util.*

class ListSuperAdminAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((SuperAdminDetails) -> Unit)? = null
  private var superAdminList: MutableList<SuperAdminDetails>? = null
  private lateinit var publicParent: ViewGroup
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    superAdminList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    publicParent = parent
    val inflater = LayoutInflater.from(parent.context)
    val viewItem = inflater.inflate(R.layout.item_layout_super_admin, parent, false)
    return HistoryViewHolder(viewItem)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val superAdminDetails = superAdminList?.get(position)
    if (getItemViewType(position) == item) {
      val historyViewHolder = holder as HistoryViewHolder
      historyViewHolder.itemTitle.text = superAdminDetails?.idUser
      historyViewHolder.itemEmail.text = superAdminDetails?.email
    }
  }

  override fun getItemCount(): Int {
    return superAdminList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == superAdminList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(superAdminDetails: SuperAdminDetails) {
    superAdminList?.add(superAdminDetails)
    superAdminList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(superAdminResult: List<SuperAdminDetails>) {
    for (result in superAdminResult) {
      add(result)
    }
  }

  fun addAt(position: Int, superAdminDetails: SuperAdminDetails) {
    superAdminList?.add(position, superAdminDetails)
  }

  fun remove(position: Int) {
    superAdminList?.removeAt(position)
    notifyItemRemoved(position)
    superAdminList?.size?.let { notifyItemRangeChanged(position, it) }
  }

  fun clear() {
    superAdminList?.clear()
  }

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemEmail: TextView = itemView.findViewById<View>(R.id.item_email) as TextView

    init {
      itemView.setOnClickListener {
        superAdminList?.get(adapterPosition)?.let { superAdminDetails ->
          superAdminDetails.position = adapterPosition
          onItemClick?.invoke(superAdminDetails)
        }
      }
    }
  }
}
