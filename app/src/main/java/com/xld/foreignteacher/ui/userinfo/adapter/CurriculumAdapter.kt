package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherDetail

/**
 * Created by cz on 4/25/18.
 */
class CurriculumAdapter(private val context: Context) : LoadMoreAdapter()  {
    private val dataList = mutableListOf<TeacherDetail.CurriculumListBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_teacher_goods, parent, false)
            return ViewHolder(view)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val holder = holder as ViewHolder
            val spannableString = SpannableString(context.getString(R.string.price_at_least, dataList[position].price))
            spannableString.setSpan(AbsoluteSizeSpan(17, true), 1, Integer.toString(dataList[position].price).length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            holder.tvPrice.text = spannableString
            holder.tvSubTitle.text = dataList[position].name
            holder.tvTitle.text = dataList[position].title
        }
    }
    fun upDataList(list: List<TeacherDetail.CurriculumListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    internal inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
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