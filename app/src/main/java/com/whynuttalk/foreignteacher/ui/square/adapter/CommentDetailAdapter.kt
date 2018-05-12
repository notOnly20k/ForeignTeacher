package com.whynuttalk.foreignteacher.ui.square.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.TimeUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.CommentReply
import com.whynuttalk.foreignteacher.ui.userinfo.StudentDetailActivity
import com.whynuttalk.foreignteacher.ui.userinfo.TeacherDetailActivity
import com.whynuttalk.foreignteacher.views.NestedGridView
import java.util.*

/**
 * Created by cz on 5/2/18.
 */
class CommentDetailAdapter(private val context: Context) : LoadMoreAdapter() {
    private val replies: MutableList<CommentReply.SquareCommentListsBean>
    private var data: CommentReply.SquareCommentBean?=null
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)

    init {
        replies = ArrayList()
    }

    fun setData(data: CommentReply) {
        this.data = data.squareComment!!
        replies.clear()
        replies.addAll(data.squareCommentLists!!)
        notifyDataSetChanged()
    }

    fun addData(data: List<CommentReply.SquareCommentListsBean>) {
        replies.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(context)
        val view: View
        when (viewType) {
            TYPE_CONTENT -> {
                view = inflater.inflate(R.layout.layout_square_content, parent, false)
                return ContentViewHolder(view)
            }
            LoadMoreAdapter.TYPE_NORMAL -> {
                view = inflater.inflate(R.layout.item_square_comment, parent, false)
                return CommentViewHolder(view)
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_CONTENT) {
            val contentViewHolder = holder as ContentViewHolder
            contentViewHolder.llBottom.visibility = View.GONE
            if (data == null) {
                return
            }
            contentViewHolder.ivHead.setImageURI(data!!.imgUrl)
            contentViewHolder.tvName.setText(data!!.nickName)
            contentViewHolder.tvContent.setText(data!!.content)
            contentViewHolder.tvTime.text = TimeUtils.getTimeDesc(data!!.createTime)
            contentViewHolder.ivHead.setOnClickListener {
                if (data!!.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", data!!.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", data!!.userId).start()
            }
            contentViewHolder.tvName.setOnClickListener {
                if (data!!.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", data!!.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", data!!.userId).start()
            }
        } else if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val reply = replies[position - 1]
            val commentViewHolder = holder as CommentViewHolder
            commentViewHolder.ivHead.setOnClickListener {
                if (reply.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", reply.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", reply.userId).start()
            }
            commentViewHolder.ivHead.setImageURI(reply.imgUrl)
            commentViewHolder.tvContent.text = reply.content
            commentViewHolder.tvTime.text = TimeUtils.getTimeDesc(reply.createTime)
            commentViewHolder.tvName.text = reply.nickName
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return TYPE_CONTENT
        return if (position == itemCount - 1) LoadMoreAdapter.TYPE_FOOTER else LoadMoreAdapter.TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return replies.size + 2
    }


    internal inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.tv_translation)
        lateinit var tvTranslation: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: NestedGridView
        @BindView(R.id.tv_location)
        lateinit var tvLocation: TextView
        @BindView(R.id.btn_like)
        lateinit var btnLike: TextView
        @BindView(R.id.btn_comment)
        lateinit var btnComment: ImageView
        @BindView(R.id.ll_bottom)
        lateinit var llBottom: LinearLayout

        init {
            ButterKnife.bind(this, view)
        }
    }

    internal inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.ll_replay)
        lateinit var llReplay: LinearLayout

        init {
            ButterKnife.bind(this, view)
        }
    }

    companion object {
        private val TYPE_CONTENT = 100
    }
}
