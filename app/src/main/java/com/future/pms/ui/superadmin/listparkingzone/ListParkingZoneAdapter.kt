package com.future.pms.ui.superadmin.listparkingzone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.future.pms.R
import com.future.pms.model.parkingzone.ParkingZoneDetails
import com.future.pms.network.NetworkConstant
import java.util.*

class ListParkingZoneAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((ParkingZoneDetails) -> Unit)? = null
  private var parkingZoneList: MutableList<ParkingZoneDetails>? = null
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
        parkingZone?.imageUrl?.let { loadImage(it, parkingZoneViewHolder.imageView) }
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

  fun add(parkingZone: ParkingZoneDetails) {
    parkingZoneList?.add(parkingZone)
    parkingZoneList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<ParkingZoneDetails>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() {
    parkingZoneList?.clear()
  }

  private fun getItem(position: Int): ParkingZoneDetails? {
    return parkingZoneList?.get(position)
  }

  private fun loadImage(imageName: String, imageView: ImageView) {
    Glide.with(publicParent).load(NetworkConstant.BASE + "img/" + imageName).transform(CenterCrop(),
        RoundedCorners(80)).placeholder(R.drawable.ic_parking_zone_default).error(
        R.drawable.ic_parking_zone_default).fallback(R.drawable.ic_parking_zone_default).into(
        imageView)
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
