package com.future.pms.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.model.customerbooking.CustomerBooking
import com.future.pms.util.Utils
import kotlinx.android.synthetic.main.item_layout_home.view.*

class HistoryAdapter(
    private val customerBookingList: List<CustomerBooking>,
    private val clickListener: (CustomerBooking) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_layout_home, parent, false)
        return BookingListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BookingListViewHolder).bind(customerBookingList[position], clickListener)
    }

    override fun getItemCount() = customerBookingList.size

    class BookingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(customerBooking: CustomerBooking, clickListener: (CustomerBooking) -> Unit) {
          itemView.item_title.text = customerBooking.parkingZoneName
          itemView.item_date.text = Utils.convertLongToTime(customerBooking.dateIn)
          itemView.setOnClickListener { clickListener(customerBooking) }
        }
    }
}