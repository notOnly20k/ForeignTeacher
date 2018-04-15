package com.xld.foreignteacher.ui.schedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/11/18.
 */
class MyOfferAdapter(private val context: Context): LoadMoreAdapter() {
    private val data: MutableList<String>
    private val activityUtil: ActivityUtil
    private val logger = LoggerFactory.getLogger("MyOfferAdapter")

    init {
        data = ArrayList()
        activityUtil = ActivityUtil.create(context)
        data.add("")
        data.add("")
        data.add("")
        data.add("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_my_offer, parent, false)
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
        }
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        init {
            ButterKnife.bind(this, view)
        }
    }
}