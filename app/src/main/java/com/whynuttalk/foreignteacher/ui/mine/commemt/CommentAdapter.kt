package com.whynuttalk.foreignteacher.ui.mine.commemt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.TeacherDetail
import com.whynuttalk.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.whynuttalk.foreignteacher.views.StarBarView
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class CommentAdapter(private val context: Context) : LoadMoreAdapter() {
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val dataList = mutableListOf<TeacherDetail.CommentListBean>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_teacher_evaluate, parent, false)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val holder = holder as ViewHolder
            holder.ivHead.setImageURI(dataList[position].userImgUrl)
            holder.sbvStarbar.setStarRating(dataList[position].score.toFloat())
            holder.tvContent.text = dataList[position].remark ?: ""
            val urls = ArrayList<String>()
            if (dataList[position].imgUrl != null && dataList[position].imgUrl!!.isNotEmpty()) {
                dataList[position].imgUrl!!.sortedBy { it.sort }.map {
                    urls.add(it.imgUrl!!)
                }
                holder.gvImg.adapter = SquareImgAdapter(urls, context)
                holder.gvImg.visibility = View.VISIBLE
            } else {
                holder.gvImg.visibility = View.GONE
            }
        }
    }

    fun upDataList(list: List<TeacherDetail.CommentListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.sbv_starbar)
        lateinit var sbvStarbar: StarBarView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: GridView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
