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
 * Created by cz on 4/15/18.
 */
class PendingDetailAdapter(val context: Context) : RecyclerView.Adapter<PendingDetailAdapter.PendingViewHold>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PendingViewHold {
        val view = LayoutInflater.from(context).inflate(R.layout.item_detail_pending_order, parent, false)
        return PendingViewHold(view)
    }

    override fun onBindViewHolder(holder: PendingViewHold?, position: Int) {

    }

    override fun getItemCount(): Int {
        return 4
    }

    class PendingViewHold(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_date)
        lateinit var tvDate: TextView
        @BindView(R.id.tv_info)
        lateinit var tvInfo: TextView
        @BindView(R.id.btn_delete)
        lateinit var btnDelete: TextView
        @BindView(R.id.btn_rate)
        lateinit var btnRate: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
