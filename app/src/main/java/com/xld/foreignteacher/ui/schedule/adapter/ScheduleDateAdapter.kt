package com.xld.foreignteacher.ui.schedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/16/18.
 */
class ScheduleDateAdapter(val context: Context): RecyclerView.Adapter<ScheduleDateAdapter.ScheduleViewHolder>() {
//    private lateinit var listener:OnItemClickListener
    private val list= mutableListOf<String>()
    init {
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
    }
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
//        holder.cdContnet.setOnClickListener {
//            //listener.onItemClick(position)
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ScheduleViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.item_schedule,parent,false)
        return  ScheduleViewHolder(view)
    }

    fun delTopItem() {
        val position = itemCount - 1
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setListener(listener:OnItemClickListener){
       // this.listener =listener
    }

    class ScheduleViewHolder(view: View):RecyclerView.ViewHolder(view){
//        @BindView(R.id.cd_content)
//        lateinit var cdContnet:CardView
//        init {
//            ButterKnife.bind(this, view)
//        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}