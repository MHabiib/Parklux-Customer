package com.future.pms.superadmin.listactivity.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.superadmin.activitydetails.model.Content
import com.future.pms.util.Utils
import java.util.*

class ListActivityAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  var onItemClick: ((Content) -> Unit)? = null
  private var bookingList: MutableList<Content>? = null
  private var isLoadingAdded = false
  private val loading = 0
  private val item = 1

  init {
    bookingList = LinkedList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val viewItem = inflater.inflate(R.layout.item_layout_activity, parent, false)
    return BookingViewHolder(viewItem)
  }

  @SuppressLint("SetTextI18n") override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
      position: Int) {
    val history = bookingList?.get(position)
    if (getItemViewType(position) == item) {
      val bookingViewHolder = holder as BookingViewHolder
      bookingViewHolder.customerName.text = history?.customerName
      bookingViewHolder.customerPhone.text = history?.customerPhone
      bookingViewHolder.slot.text = history?.slotName
      bookingViewHolder.parkingZone.text = history?.parkingZoneName
      bookingViewHolder.totalPrice.text = String.format("IDR %s",
          history?.totalPrice?.toInt()?.let {
            Utils.thousandSeparator(it)
          })
      bookingViewHolder.timeRang.text = String.format("%s\n%s",
          history?.dateIn?.let { Utils.convertLongToTimeShortMonth(it) },
          history?.dateOut?.let { Utils.convertLongToTimeShortMonth(it) })
      if (history?.dateOut == null) {
        bookingViewHolder.line.setBackgroundResource(R.color.red)
        bookingViewHolder.status.text = "Ongoing"
      } else {
        bookingViewHolder.line.setBackgroundResource(R.color.gold)
        bookingViewHolder.status.text = "Completed"
      }
    }
  }

  override fun getItemCount(): Int {
    return bookingList?.size ?: 0
  }

  override fun getItemViewType(position: Int): Int {
    return if (position == bookingList?.size?.minus(1) && isLoadingAdded) loading else item
  }

  fun add(booking: Content) {
    bookingList?.add(booking)
    bookingList?.size?.minus(1)?.let { notifyItemInserted(it) }
  }

  fun addAll(bookingResult: List<Content>) {
    for (result in bookingResult) {
      add(result)
    }
  }

  fun addAt(position: Int, booking: Content) {
    bookingList?.add(position, booking)
  }

  fun remove(position: Int) {
    bookingList?.removeAt(position)
    notifyItemRemoved(position)
    bookingList?.size?.let { notifyItemRangeChanged(position, it) }
  }

  fun clear() {
    bookingList?.clear()
  }

  inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val customerName: TextView = itemView.findViewById<View>(R.id.tv_customer_name) as TextView
    val customerPhone: TextView = itemView.findViewById<View>(R.id.tv_customer_phone) as TextView
    val slot: TextView = itemView.findViewById<View>(R.id.tv_slot) as TextView
    val totalPrice: TextView = itemView.findViewById<View>(R.id.tv_total_price) as TextView
    val timeRang: TextView = itemView.findViewById<View>(R.id.tv_time_range) as TextView
    val parkingZone: TextView = itemView.findViewById<View>(R.id.tv_parking_zone_name) as TextView
    val status: TextView = itemView.findViewById<View>(R.id.status) as TextView
    val line: View = itemView.findViewById(R.id.line) as View

    init {
      itemView.setOnClickListener {
        bookingList?.get(adapterPosition)?.let { it ->
          it.position = adapterPosition
          onItemClick?.invoke(it)
        }
      }
    }
  }
}
