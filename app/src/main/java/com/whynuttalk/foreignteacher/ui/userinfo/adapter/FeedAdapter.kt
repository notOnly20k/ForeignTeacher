package com.whynuttalk.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.TimeUtils
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.SquareListBean
import com.whynuttalk.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.whynuttalk.foreignteacher.views.NestedGridView
import java.util.*

/**
 * Created by cz on 4/25/18.
 */
class FeedAdapter(private val context: Context) : LoadMoreAdapter() {
    private val dataList = mutableListOf<SquareListBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_teacher_moment, parent, false)
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
            holder.rlContent.setBackgroundColor(context.resources.getColor(R.color.white))
            holder.tvContent.text = dataList[position].content
            holder.tvTime.text = TimeUtils.getTimeHM(dataList[position].createTime)
//            holder.tvContent.setOnLongClickListener {
//                //TODO 翻译成功后显示
////            holder.tvTranslation!!.visibility = View.VISIBLE
////            holder.tvTranslation!!.text = "翻译结果"
//                true
//            }
            val urls = ArrayList<String>()
            if (dataList[position].imgUrl != null && dataList[position].imgUrl!!.isNotEmpty()) {
                dataList[position].imgUrl!!.sortedBy { it.sort }.map {
                    urls.add(it.imgUrl!!)
                }
                holder.gvImg.adapter = SquareImgAdapter(urls, context)
                holder.gvImg.visibility = View.VISIBLE
            }
        }
    }

    fun upDataList(list: List<SquareListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        //        @BindView(R.id.tv_translation)
//        lateinit var tvTranslation: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.rl_content)
        lateinit var rlContent: RelativeLayout
        @BindView(R.id.gv_img)
        lateinit var gvImg: NestedGridView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
