package com.future.pms.ui.superadmin.listuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.model.admin.AdminDetails
import com.future.pms.util.Utils
import java.util.*

class ListAdminAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((AdminDetails) -> Unit)? = null
  private var parkingZoneList: MutableList<AdminDetails>? = null
  private lateinit var publicParent: ViewGroup
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    parkingZoneList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    publicParent = parent
    val inflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      item -> {
        val viewItem = inflater.inflate(R.layout.item_layout_parking_zone, parent, false)
        HistoryViewHolder(viewItem)
      }
      else -> {
        val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
        LoadingViewHolder(viewLoading)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val parkingZone = parkingZoneList?.get(position)
    when (getItemViewType(position)) {
      item -> {
        val parkingZoneViewHolder = holder as HistoryViewHolder
        parkingZoneViewHolder.itemTitle.text = parkingZone?.name
        parkingZoneViewHolder.itemEmail.text = parkingZone?.emailAdmin
        parkingZoneViewHolder.itemPhone.text = parkingZone?.phoneNumber
        parkingZone?.imageUrl?.let { loadImage(publicParent, it, parkingZoneViewHolder.imageView) }
      }

      loading -> {
        val loadingViewHolder = holder as LoadingViewHolder
        loadingViewHolder.progressBar.visibility = View.VISIBLE
      }
    }
  }

  override fun getItemCount(): Int {
    return parkingZoneList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == parkingZoneList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(parkingZone: AdminDetails) {
    parkingZoneList?.add(parkingZone)
    parkingZoneList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<AdminDetails>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() {
    parkingZoneList?.clear()
  }

  private fun getItem(position: Int): AdminDetails? {
    return parkingZoneList?.get(position)
  }

  private fun loadImage(viewGroup: ViewGroup, imageName: String, imageView: ImageView) {
    Utils.imageLoader(viewGroup, imageName, imageView)
  }

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemEmail: TextView = itemView.findViewById<View>(R.id.item_email) as TextView
    val itemPhone: TextView = itemView.findViewById<View>(R.id.item_phone) as TextView
    val imageView: ImageView = itemView.findViewById<View>(R.id.iv_parking_zone_image) as ImageView

    init {
      itemView.setOnClickListener {
        parkingZoneList?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
      }
    }
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById<View>(
        R.id.loadmore_progress) as ProgressBar
  }

}
