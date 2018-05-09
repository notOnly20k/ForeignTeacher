package com.xld.foreignteacher.ui.order.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.TimeUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.data.BookInfo
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/3/18.
 */
class OrderDetailAdapter(private val context: Context) : RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>() {
    val logger = LoggerFactory.getLogger("OrderDetailAdapter")
    private var data = mutableListOf<BookInfo>()
    var weeks = 0
    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        val c = Calendar.getInstance()
        c.time = TimeUtils.parseTaskTimeYMD(data[position].day)
        val week = TimeUtils.getWeek(c.time)
        val startTime = data[position].startTime.toString().split(".")
        val endTime = data[position].endTime.toString().split(".")
        //val time=if (startTime.size)
        var halfStartTime = "0"
        var halfEndTime = "0"
        if (startTime[1] == "5") {
            halfStartTime = "3"
        }
        if (endTime[1] == "5") {
            halfEndTime = "3"
        }

        holder.tvTime.text = "$week(${startTime[0]}:${halfStartTime}0-${endTime[0]}:${halfEndTime}0)"
        if (weeks == 0) {
            holder.tvLabel.visibility = View.GONE
        } else {
            holder.tvLabel.visibility = View.VISIBLE
            holder.tvLabel.text = "Booked for $weeks weeks"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): OrderDetailViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false)
        return OrderDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list: List<BookInfo>, week: Int) {
        data.clear()
        data.addAll(list)
        weeks = week
        notifyDataSetChanged()
    }

    inner class OrderDetailViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_label)
        lateinit var tvLabel: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
