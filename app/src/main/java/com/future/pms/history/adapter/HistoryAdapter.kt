package com.future.pms.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.history.model.BookingHistory
import com.future.pms.util.Utils
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((BookingHistory) -> Unit)? = null
  private var historyList: MutableList<BookingHistory>? = LinkedList()
  private lateinit var publicParent: ViewGroup
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    publicParent = parent
    return HistoryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_layout_home, parent, false))
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val booking = historyList?.get(position)
    if (getItemViewType(position) == item) {
      val historyViewHolder = holder as HistoryViewHolder
      historyViewHolder.itemTitle.text = booking?.parkingZoneName
      historyViewHolder.itemDate.text = booking?.dateIn?.let { Utils.convertLongToTime(it) }
      booking?.imageUrl?.let {
        loadImage(publicParent, it, historyViewHolder.imageView)
      }
    }
  }

  override fun getItemCount(): Int = historyList?.size ?: 0

  override fun getItemViewType(position: Int): Int = if (position == historyList?.size?.minus(
          1) && isLoadingAdded) loading else item

  fun add(booking: BookingHistory) {
    historyList?.add(booking)
    historyList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<BookingHistory>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun clear() = historyList?.clear()

  private fun loadImage(view: View, imageName: String, imageView: ImageView) = Utils.imageLoader(
      view, imageName, imageView)

  inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemTitle: TextView = itemView.findViewById<View>(R.id.item_title) as TextView
    val itemDate: TextView = itemView.findViewById<View>(R.id.item_date) as TextView
    val imageView: ImageView = itemView.findViewById<View>(R.id.iv_parking_zone_image) as ImageView

    init {
      itemView.setOnClickListener {
        historyList?.get(adapterPosition)?.let { bookingHistory ->
          onItemClick?.invoke(bookingHistory)
        }
      }
    }
  }

}
