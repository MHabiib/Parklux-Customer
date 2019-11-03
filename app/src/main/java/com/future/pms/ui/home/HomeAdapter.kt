package com.future.pms.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.future.pms.R
import com.future.pms.model.Post

class HomeAdapter(private val context: Context?, private val list: MutableList<Post>): RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = list[position]

        // holder!!.bind(post)
        holder.title.text = post.title
        holder.body.text = post.body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_layout_home, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var layout: ConstraintLayout = itemView.findViewById(R.id.item_layout)
        val title: TextView = itemView.findViewById(R.id.item_title)
        val body: TextView = itemView.findViewById(R.id.item_body)

        fun bind(item: Post) {}
    }

/*    interface onItemClickListener {
        fun itemDetail(postId : String)
    }*/
}