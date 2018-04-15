package com.xld.foreignteacher.ui.mine.commemt

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
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.xld.foreignteacher.views.StarBarView

/**
 * Created by cz on 4/2/18.
 */
class CommentAdapter(private val context: Context, private val data: List<String>) : LoadMoreAdapter() {
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
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
        return super.getItemCount() + 5
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val commentHolder = holder as ViewHolder
            val urls = ArrayList<String>()
            urls.add("")
            urls.add("")
            urls.add("")
            urls.add("")
            urls.add("")
            holder.gvImg.adapter = SquareImgAdapter(urls, context)
            holder.gvImg.visibility = View.VISIBLE
        }
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
