package com.xld.foreignteacher.ui.mine.wallet

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
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherRecord
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/13/18.
 */
class TransactionsAdapter(val context: Context) : LoadMoreAdapter() {
    private val dataList: MutableList<TeacherRecord> = mutableListOf()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private var onItemClickListener: OnItemClickListener? = null
    private val logger = LoggerFactory.getLogger("SquareAdapter")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_with_draw_detail, parent, false)
            return TransViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            LoadMoreAdapter.TYPE_FOOTER
        else
            LoadMoreAdapter.TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + dataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val myholder = holder as TransViewHolder
            myholder.tvTitle.text = dataList[position].remark
            val status = dataList[position].status
            if (status == 1) {
                myholder.tvMoney.text = "-${dataList[position].money}"
            } else {
                myholder.tvMoney.text = "+${dataList[position].money}"
            }
            myholder.tvTime.text = TimeUtils.getTimeYMDCN(dataList[position].createTime)
        } else {
            holder.itemView.setOnClickListener {
                onItemClickListener?.loadMore()
            }
        }
    }


    fun updateList(list: List<TeacherRecord>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    interface OnItemClickListener {
        fun loadMore()
    }

    inner class TransViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
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
