package com.future.pms.ui.history

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
import com.future.pms.model.history.Content
import com.future.pms.util.Utils
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((Content) -> Unit)? = null
  private var historyList: MutableList<Content>? = null
  private lateinit var publicParent: ViewGroup
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    historyList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    publicParent = parent
    val inflater = LayoutInflater.from(parent.context)
    return when (viewType) {
      item -> {
        val viewItem = inflater.inflate(R.layout.item_layout_home, parent, false)
        HistoryViewHolder(viewItem)
      }
      else -> {
        val viewLoading = inflater.inflate(R.layout.item_progress, parent, false)
        LoadingViewHolder(viewLoading)
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val booking = historyList?.get(position)
    when (getItemViewType(position)) {
      item -> {
        val historyViewHolder = holder as HistoryViewHolder
        historyViewHolder.itemTitle.text = booking?.parkingZoneName
        historyViewHolder.itemDate.text = booking?.dateIn?.let { Utils.convertLongToTime(it) }
      }

      loading -> {
        val loadingViewHolder = holder as LoadingViewHolder
        loadingViewHolder.progressBar.visibility = View.VISIBLE
      }
    }
  }

  override fun getItemCount(): Int {
    return historyList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == historyList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(booking: Content) {
    historyList?.add(booking)
    historyList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<Content>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() {
    historyList?.clear()
  }

  private fun getItem(position: Int): Content? {
    return historyList?.get(position)
  }

  fun loadImage(imageName: String, imageView: ImageView) {
    Glide.with(publicParent).load(
        "http://192.168.18.50:8088/qr/Grand Indonesia - Level - 2 12.png").transform(CenterCrop(),
        RoundedCorners(80)).placeholder(R.drawable.ic_image_place_holder).error(
        R.drawable.ic_image_place_holder).fallback(R.drawable.ic_image_place_holder).into(imageView)
  }

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemDate: TextView = itemView.findViewById<View>(R.id.item_date) as TextView
    private val imageView: ImageView = itemView.findViewById<View>(
        R.id.iv_parking_zone_image) as ImageView

    init {
      itemView.setOnClickListener {
        historyList?.get(adapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
      }
      loadImage("feelspecial", imageView)
    }
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById<View>(
        R.id.loadmore_progress) as ProgressBar
  }

}
