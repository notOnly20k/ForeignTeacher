package com.whynuttalk.foreignteacher.ui.schedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.TeacherCurriculum
import com.whynuttalk.foreignteacher.ui.schedule.AddOfferActivity
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/11/18.
 */
class MyOfferAdapter(private val context: Context) : RecyclerView.Adapter<MyOfferAdapter.ViewHolder>() {
    private val data: MutableList<TeacherCurriculum> = mutableListOf()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("MyOfferAdapter")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOfferAdapter.ViewHolder? {

        val view = LayoutInflater.from(context).inflate(R.layout.item_my_offer, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyOfferAdapter.ViewHolder, position: Int) {
        val order = data[position]
        holder.itemView.setOnClickListener {
            activityUtil.go(AddOfferActivity::class.java).put("type",AddOfferActivity.EDIT).put("id",order.id).start()
        }
        holder.tvTitle.text = order.title
        holder.tvInfo.text = order.eName
        val spannableString = SpannableString(context.getString(R.string.price_at_least, order.price))
        spannableString.setSpan(AbsoluteSizeSpan(17, true), 1, Integer.toString(order.price).length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        holder.tvPrice.text = spannableString
        holder.tvPeople.text = "people:" + order.peopleNum.toString()
        holder.tvSold.text = "Sold:" + order.orderNum.toString()
        holder.tvTotalIncome.text = "Total income:￥" + order.payPrice.toString()
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_info)
        lateinit var tvInfo: TextView
        @BindView(R.id.tv_price)
        lateinit var tvPrice: TextView
        @BindView(R.id.view)
        lateinit var view: View
        @BindView(R.id.tv_people)
        lateinit var tvPeople: TextView
        @BindView(R.id.tv_sold)
        lateinit var tvSold: TextView
        @BindView(R.id.tv_total_income)
        lateinit var tvTotalIncome: TextView
        @BindView(R.id.img_line)
        lateinit var imgLine: ImageView

        init {
            ButterKnife.bind(this, view)
        }
    }

    fun setData(it: List<TeacherCurriculum>) {
        data.clear()
        data.addAll(it)
        notifyDataSetChanged()
    }
}
