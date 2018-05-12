package com.whynuttalk.foreignteacher.ui.mine.wallet

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.TimeUtils
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.WithDrawDetail
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/13/18.
 */
class WithDrawDetailAdapter(val context: Context) : LoadMoreAdapter() {
    private val data = mutableListOf<WithDrawDetail>()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("SquareAdapter")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_with_draw_detail, parent, false)
            return ViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            TYPE_FOOTER
        else
            TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val holder = holder as ViewHolder
            holder.tvTitle.text = when (data[position].state) {
                1 -> {
                    "Pending"
                }
                2 -> {
                    "WithDraw"
                }
                3 -> {
                    "Refused"
                }
                else -> {
                    ""
                }
            }
            holder.tvMoney.text=data[position].withdrawMoney.toString()
            holder.tvTime.text=TimeUtils.getTimeYMDCN(data[position].createdate)
        }
    }

    fun updateList(list: List<WithDrawDetail>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_money)
        lateinit var tvMoney: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
