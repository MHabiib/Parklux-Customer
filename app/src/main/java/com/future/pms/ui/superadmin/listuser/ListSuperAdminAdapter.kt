package com.future.pms.ui.superadmin.listuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.model.superadmin.SuperAdminDetails
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
    return when (viewType) {
      item -> {
        val viewItem = inflater.inflate(R.layout.item_layout_super_admin, parent, false)
        HistoryViewHolder(viewItem)
      }
      else -> {
        val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
        LoadingViewHolder(viewLoading)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val superAdminDetails = superAdminList?.get(position)
    when (getItemViewType(position)) {
      item -> {
        val historyViewHolder = holder as HistoryViewHolder
        historyViewHolder.itemTitle.text = superAdminDetails?.idUser
        historyViewHolder.itemEmail.text = superAdminDetails?.email
      }

      loading -> {
        val loadingViewHolder = holder as LoadingViewHolder
        loadingViewHolder.progressBar.visibility = View.VISIBLE
      }
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

  fun clear() {
    superAdminList?.clear()
  }

  private fun getItem(position: Int): SuperAdminDetails? {
    return superAdminList?.get(position)
  }

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemEmail: TextView = itemView.findViewById<View>(R.id.item_email) as TextView

    init {
      itemView.setOnClickListener {
        superAdminList?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
      }
    }
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById<View>(
        R.id.loadmore_progress) as ProgressBar
  }

}
