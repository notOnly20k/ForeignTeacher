package com.xld.foreignteacher.ui.schedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/16/18.
 */
class MyGroupOfferAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
        data.add("")

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {

        val view = LayoutInflater.from(context).inflate(R.layout.item_my_group_order, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {

        init {
            ButterKnife.bind(this, view)
        }
    }
}