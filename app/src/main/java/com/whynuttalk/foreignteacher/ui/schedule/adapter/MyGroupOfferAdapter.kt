package com.whynuttalk.foreignteacher.ui.schedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.TeacherFight
import com.whynuttalk.foreignteacher.ui.schedule.PreviewGroupOrderActivity
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 4/16/18.
 */
class MyGroupOfferAdapter(private val context: Context) : RecyclerView.Adapter<MyGroupOfferAdapter.ViewHolder>() {
    private val data: MutableList<TeacherFight> = mutableListOf()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("MyOfferAdapter")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGroupOfferAdapter.ViewHolder? {

        val view = LayoutInflater.from(context).inflate(R.layout.item_my_group_order, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(list: List<TeacherFight>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyGroupOfferAdapter.ViewHolder, position: Int) {
        val fight = data[position]
        val formater = SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
        val f = SimpleDateFormat("MM-dd", Locale.CHINA)
        val f1 = SimpleDateFormat("HH:mm", Locale.CHINA)
        holder.ivImg.setImageURI(fight.backgroundCourse)
        holder.tvTitle.text = fight.title
        holder.tvLocation.text = fight.address
        holder.tvSubTime.text = "Submit time:" + formater.format(fight.createTime)
        val week = f.format(fight.openingTime)
        val start = f1.format(fight.openingTime)
        val end = f1.format(fight.endTime)
        holder.tvState.text = fight.remark
        holder.tvDatetime.text = "Timer: $week $start ~ $end"
        holder.rlContent.setOnClickListener {
            activityUtil.go(PreviewGroupOrderActivity::class.java).put("id", fight.id).put("type",11).start()
        }
        when (fight.status) {
            1 -> {
                var backDra = context.resources.getDrawable(R.mipmap.icon_pending)
                holder.tvTypeTag.setCompoundDrawablesWithIntrinsicBounds(backDra, null, null, null)
                holder.tvTypeTag.text = "Pending"
                holder.tvTypeTag.setBackgroundColor(context.resources.getColor(R.color.color_red_ff8a00))
                holder.btnCancel.text = "Apply for cancel"
                holder.btnCancel.setOnClickListener{
                    callback?.onCancelItem(fight.id)
                }
            }
            2, 3, 4, 5 -> {
                var backDra = context.resources.getDrawable(R.mipmap.passed)
                holder.tvTypeTag.setCompoundDrawablesWithIntrinsicBounds(backDra, null, null, null)
                holder.tvTypeTag.text = "Passed"
                holder.tvTypeTag.setBackgroundColor(context.resources.getColor(R.color.color_red_ff4a26))
                holder.btnCancel.visibility=View.GONE
            }
            7 -> {
                holder.tvTypeTag.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                holder.tvTypeTag.text = "Deleted"
                holder.tvTypeTag.setBackgroundColor(context.resources.getColor(R.color.gray_pressed))
            }
            6, 8, 9 -> {
                var backDra = context.resources.getDrawable(R.mipmap.bypassed)
                holder.tvTypeTag.setCompoundDrawablesWithIntrinsicBounds(backDra, null, null, null)
                holder.tvTypeTag.text = "Bypassed"
                holder.tvTypeTag.setBackgroundColor(context.resources.getColor(R.color.gray_pressed))
                holder.btnCancel.text = "Delete"
                holder.btnCancel.setOnClickListener {
                    callback?.onDeletItem(fight.id)
                }
            }
        }
    }


    interface OnItemClickCallback {
        fun onDeletItem(id: Int)
        fun onCancelItem(id: Int)
    }
    private var callback: OnItemClickCallback? = null
    fun setCallback(callback: OnItemClickCallback) {
        this.callback = callback
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_img)
        lateinit var ivImg: SimpleDraweeView
        @BindView(R.id.tv_type_tag)
        lateinit var tvTypeTag: TextView
        @BindView(R.id.tv_sub_time)
        lateinit var tvSubTime: TextView
        @BindView(R.id.tv_state)
        lateinit var tvState: TextView
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_datetime)
        lateinit var tvDatetime: TextView
        @BindView(R.id.tv_location)
        lateinit var tvLocation: TextView
        @BindView(R.id.view)
        lateinit var view: View
        @BindView(R.id.btn_cancel)
        lateinit var btnCancel: TextView
        @BindView(R.id.rl_item)
        lateinit var rlContent: RelativeLayout

        init {
            ButterKnife.bind(this, view)
        }
    }
}
