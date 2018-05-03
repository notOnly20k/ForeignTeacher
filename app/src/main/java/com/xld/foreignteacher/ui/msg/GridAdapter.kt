package com.xld.foreignteacher.ui.msg

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/27/18.
 */
class GridAdapter:RecyclerView.Adapter<GridAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_schedule_text,parent,false)
        return MyViewHolder(view)
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}