package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherDetail

/**
 * Created by cz on 4/2/18.
 */
class TeacherGoodsAdapter(private val context: Context) : BaseAdapter() {
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val dataList = mutableListOf<TeacherDetail.CurriculumListBean>()

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_goods, null)
        }
        val holder = ViewHolder(convertView!!)
        val spannableString = SpannableString(context.getString(R.string.price_at_least, dataList[position].price))
        spannableString.setSpan(AbsoluteSizeSpan(17, true), 1, Integer.toString(dataList[position].price).length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        holder.tvPrice.text = spannableString
        holder.tvSubTitle.text = dataList[position].name
        holder.tvTitle.text = dataList[position].title
        return convertView
    }

    fun upDataList(list: List<TeacherDetail.CurriculumListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    internal inner class ViewHolder(view: View) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_sub_title)
        lateinit var tvSubTitle: TextView
        @BindView(R.id.tv_price)
        lateinit var tvPrice: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
