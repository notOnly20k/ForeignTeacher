package com.xld.foreignteacher.ui.square.adapter

import android.content.Context
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
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.userinfo.StudentDetailActivity
import com.xld.foreignteacher.views.NestedGridView
import java.util.*

/**
 * Created by cz on 4/1/18.
 */
class SquareDetailAdapter(private val context: Context) : LoadMoreAdapter() {
    private val comments: MutableList<String>
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)

    init {
        comments = ArrayList()
        comments.add("")
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
            //todo 渲染
        } else if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {
            val commentViewHolder = holder as CommentViewHolder

            val backDra=context.resources.getDrawable(R.mipmap.icon_woman)
            backDra.setBounds( backDra.minimumWidth, 0,0 ,backDra.minimumHeight)
            commentViewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null,null,backDra,null)
            commentViewHolder.tvName.compoundDrawablePadding=backDra.minimumHeight/2
            commentViewHolder.ivHead.setOnClickListener { activityUtil.go(StudentDetailActivity::class.java).start() }

            commentViewHolder.llReplay.visibility =View.VISIBLE
            val tv = TextView(context)
            val tag1 = "Mark"
            val reply = SpannableString("$tag1 reply: Wahu ,nice to meet you la la la la l alal")
            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), 0, tag1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            reply.setSpan(StyleSpan(Typeface.BOLD), 0, tag1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), tag1.length, tag1.length + 8, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

            tv.text = reply
            commentViewHolder.llReplay.addView(tv)
        }
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

    companion object {
        private val TYPE_CONTENT = 100
    }
}
