package com.xld.foreignteacher.ui.square.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.SquareDetail
import com.xld.foreignteacher.ui.square.CommentDetailActivity
import com.xld.foreignteacher.ui.userinfo.StudentDetailActivity
import com.xld.foreignteacher.ui.userinfo.TeacherDetailActivity
import com.xld.foreignteacher.views.NestedGridView

/**
 * Created by cz on 4/1/18.
 */
class SquareDetailAdapter(val context: Context) : LoadMoreAdapter() {
    private val TYPE_CONTENT = 100
    private var comments: MutableList<SquareDetail.SquareCommentListBean> = mutableListOf()
    private var data: SquareDetail? = null

    private var activityUtil: ActivityUtil = ActivityUtil.create(context)

    fun setData(data: SquareDetail) {
        this.data = data
        comments.clear()
        comments.addAll(data.squareCommentList!!)
        notifyDataSetChanged()
    }

    fun addData(data: List<SquareDetail.SquareCommentListBean>) {
        comments.addAll(data)
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
            if (data == null)
                return
            val contentViewHolder = holder as ContentViewHolder
            contentViewHolder.ivHead!!.setImageURI(data!!.teacherImgUrl)
            contentViewHolder.tvName!!.setText(data!!.nickName)
            contentViewHolder.tvContent!!.setText(data!!.content)
            contentViewHolder.tvTranslation!!.setText(data!!.contents)
            contentViewHolder.tvTime!!.text = TimeUtils.getTimeDesc(data!!.createTime)
            contentViewHolder.tvLocation!!.text = data!!.address
            contentViewHolder.btnLike.text = data!!.giveThumNum.toString()
            contentViewHolder.btnLike.setCompoundDrawablesWithIntrinsicBounds(
                    if (data!!.giveThum) context.resources.getDrawable(R.mipmap.icon_like) else context.resources.getDrawable(R.mipmap.empty_like), null, null, null)
            if (data!!.imgUrl != null && data!!.imgUrl!!.isNotEmpty()) { //图片
                val list = mutableListOf<String>()
                data!!.imgUrl!!.sortedBy { it.sort }.map { list.add(it.imgUrl!!) }
                contentViewHolder.gvImg!!.setAdapter(SquareImgAdapter(list, context))
                contentViewHolder.gvImg!!.setVisibility(View.VISIBLE)
            } else
                contentViewHolder.gvImg!!.setVisibility(View.GONE)
            contentViewHolder.tvContent!!.setOnLongClickListener {
                //翻译
                contentViewHolder.tvTranslation!!.visibility = View.VISIBLE
                contentViewHolder.tvTranslation!!.setText(data!!.contents)
                true
            }
            contentViewHolder.ivHead!!.setOnClickListener { activityUtil.go(TeacherDetailActivity::class.java).put("id", data!!.teacherId).start() }
            contentViewHolder.tvName!!.setOnClickListener { activityUtil.go(TeacherDetailActivity::class.java).put("id", data!!.teacherId).start() }

            contentViewHolder.tvLocation!!.setOnClickListener {
                //todo 跳地图
            }
            contentViewHolder.btnLike!!.setOnClickListener {
                clickCallback?.doLike(data!!.id)
            }
            contentViewHolder.btnComment!!.setOnClickListener { clickCallback!!.onComment() }
        } else if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val comment = comments[position - 1]
            val commentViewHolder = holder as CommentViewHolder
            commentViewHolder.ivHead!!.setOnClickListener {
                if (comment.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", comment.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", comment.userId).start()
            }
            commentViewHolder.ivHead!!.setImageURI(comment.imgUrl)
            commentViewHolder.tvContent!!.text = comment.content
            commentViewHolder.tvTime!!.text = TimeUtils.getTimeDesc(comment.createTime)
            commentViewHolder.tvName!!.text = comment.nickName
            commentViewHolder.tvName!!.setOnClickListener {
                if (comment.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", comment.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", comment.userId).start()
            }
            commentViewHolder.ivHead!!.setOnClickListener {
                if (comment.type === 1)
                    activityUtil.go(StudentDetailActivity::class.java).put("id", comment.userId).start()
                else
                    activityUtil.go(TeacherDetailActivity::class.java).put("id", comment.userId).start()
            }
            commentViewHolder.tvContent!!.setOnClickListener { clickCallback!!.onReply(comments[position - 1].id) }
            commentViewHolder.llReplay!!.removeAllViews()
            if (comment.squareCommentLists!!.isNotEmpty()) {
                val colorSpan = ForegroundColorSpan(Color.parseColor("#333333"))
                if (comment.squareCommentListNum > 3) {
                    for (i in 0..2) {
                        val reply = comment.squareCommentLists!![i]
                        val view = LayoutInflater.from(context).inflate(R.layout.item_detail_reply, null) as TextView
                        view.setOnClickListener {
                            clickCallback?.onReply(comment.id)
                        }
                        val commentName = reply.nickName
                        val content = reply.content
                        val spannableString: SpannableString
                        spannableString = SpannableString(commentName + " reply：" + content)
                        spannableString.setSpan(colorSpan, 0, commentName!!.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, commentName.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        view.text = spannableString
                        commentViewHolder.llReplay!!.addView(view)
                    }
                    val view = LayoutInflater.from(context).inflate(R.layout.item_detail_reply, null) as TextView
                    view.text = "Read more"
                    view.setTextColor(Color.parseColor("#ff8400"))
                    view.setOnClickListener { activityUtil.go(CommentDetailActivity::class.java).put("id", comment.id).start() }
                    commentViewHolder.llReplay!!.addView(view)
                } else {
                    for (reply in comment.squareCommentLists!!) {
                        val view = LayoutInflater.from(context).inflate(R.layout.item_detail_reply, null) as TextView
                        view.setOnClickListener {
                            clickCallback?.onReply(comment.id)
                        }
                        val commentName = reply.nickName
                        val content = reply.content
                        val spannableString: SpannableString
                        spannableString = SpannableString(commentName + " reply：" + content)
                        spannableString.setSpan(colorSpan, 0, commentName!!.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, commentName!!.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        view.text = spannableString
                        commentViewHolder.llReplay!!.addView(view)
                    }
                }
                commentViewHolder.llReplay!!.visibility = View.VISIBLE
            } else
                commentViewHolder.llReplay!!.visibility = View.GONE
        }
    }

    interface OnClickCallback {

        fun onComment()

        fun onReply(commentId: Int)

        fun doLike(squareId: Int)
    }

    private var clickCallback: OnClickCallback? = null

    fun setOnClickCallback(clickCallback: OnClickCallback) {
        this.clickCallback = clickCallback
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0)
            return TYPE_CONTENT
        return if (position == itemCount - 1) LoadMoreAdapter.TYPE_FOOTER else LoadMoreAdapter.TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return comments.size + 2
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
}
