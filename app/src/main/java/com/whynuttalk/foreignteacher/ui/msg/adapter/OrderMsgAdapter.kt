package com.whynuttalk.foreignteacher.ui.msg.adapter

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
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.OrderMessage

/**
 * Created by cz on 5/7/18.
 */
class OrderMsgAdapter(val context: Context): LoadMoreAdapter() {
    private var data: MutableList<OrderMessage> = mutableListOf()
    private var activityUtil: ActivityUtil= ActivityUtil.create(context)


    fun setData(data: List<OrderMessage>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<OrderMessage>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_order_msg, parent, false)
            return ViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val viewHolder = holder as ViewHolder
            viewHolder.tvTime.text = TimeUtils.getTimeDesc(data[position].addtime)
            viewHolder.tvTitle.text = data[position].title
            viewHolder.tvContent.text = data[position].content
        }

    }

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                // TODO: 2018/4/16 订单详情
            }
        }
    }
}
