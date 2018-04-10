package com.xld.foreignteacher.ui.report.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.xld.foreignteacher.R
import com.xld.foreignteacher.data.DeclineReason

/**
 * Created by cz on 4/8/18.
 */
class DeclinedAdapter(val context: Context, var list: List<DeclineReason>) : RecyclerView.Adapter<DeclinedAdapter.DeclinedViewHolder>() {
    private var onItemSelectedListener: OnItemSelectedListener? = null
    override fun onBindViewHolder(holder: DeclinedViewHolder, position: Int) {
        holder.tvReason.text = list[position].reason
        if (!list[position].isSelect) {
            holder.tvReason.setTextColor(context.resources.getColor(R.color.textColor))
            holder.imgLabel.visibility = View.GONE
            holder.rlItem.setBackgroundColor(context.resources.getColor(R.color.white))
        }else{
            holder.tvReason.setTextColor(context.resources.getColor(R.color.yellow_ffcc00))
            holder.imgLabel.visibility = View.VISIBLE
            holder.rlItem.background=context.resources.getDrawable(R.drawable.bg_declined_selected)
        }
        holder.rlItem.setOnClickListener {
            onItemSelectedListener?.onItemSelected(list[position],position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setListener(onItemSelectedListener: OnItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    fun upDateList(list: List<DeclineReason>){
        this.list =list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DeclinedViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_declined_reason, parent, false)
        return DeclinedViewHolder(view)
    }

    interface OnItemSelectedListener {
        fun onItemSelected(declineReason: DeclineReason, position: Int)
    }


    class DeclinedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_reason)
        lateinit var tvReason: TextView
        @BindView(R.id.img_label)
        lateinit var imgLabel: ImageView
        @BindView(R.id.rl_item)
        lateinit var rlItem: RelativeLayout

        init {
            ButterKnife.bind(this, view)
        }
    }
}
