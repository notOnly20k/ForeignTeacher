package com.xld.foreignteacher.ui.order.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/3/18.
 */
class OrderDetailAdapter(private val context: Context): RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {
    override fun onBindViewHolder(holder: OrderDetailViewHolder?, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OrderDetailViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false)
        return OrderDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class OrderDetailViewHolder constructor(view: View): RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_label)
        lateinit var tvLabel: TextView
        init {
            ButterKnife.bind(this, view)
        }
    }
}