package com.whynuttalk.foreignteacher.ui.square.adapter

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.support.v4.app.FragmentManager
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
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.TimeUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.SquareDate
import com.whynuttalk.foreignteacher.ui.square.SquareDetailActivity
import com.whynuttalk.foreignteacher.ui.userinfo.TeacherDetailActivity
import com.whynuttalk.foreignteacher.views.NestedGridView
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/1/18.
 */
class SquareAdapter(private val context: Context, private val fragmentManager: FragmentManager) : LoadMoreAdapter() {

    private val data: MutableList<SquareDate> = mutableListOf()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("SquareAdapter")
    private var openContent = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_square, parent, false)
            return ViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            TYPE_FOOTER
        else
            TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val iconLike = context.resources.getDrawable(R.mipmap.icon_like)
            iconLike.setBounds(0, 0, iconLike.minimumHeight, iconLike.minimumHeight)
            val iconEmptyLike = context.resources.getDrawable(R.mipmap.empty_like)
            iconEmptyLike.setBounds(0, 0, iconEmptyLike.minimumHeight, iconEmptyLike.minimumHeight)


            val viewHolder = holder as ViewHolder
            val squareDate = data[position]
            viewHolder.tvName.text = squareDate.nickName
            viewHolder.tvTime.text = TimeUtils.getTimeHM(squareDate.createTime)
            viewHolder.tvContent.text = squareDate.content
            viewHolder.tvContent.maxLines = 4
            viewHolder.tvLocation.text = squareDate.address
            viewHolder.ivHead.setImageURI(squareDate.teacherImgUrl)
            viewHolder.btnLike.text = squareDate.giveThumNum.toString()
            if (squareDate.giveThum) {
                viewHolder.btnLike.setCompoundDrawables(iconLike, null, null, null)
            } else {
                viewHolder.btnLike.setCompoundDrawables(iconEmptyLike, null, null, null)
            }

            if (viewHolder.tvContent.lineCount > 3) {
                viewHolder.tvContentFull.paint.flags = Paint.UNDERLINE_TEXT_FLAG
                viewHolder.tvContentFull.paint.isAntiAlias = true
                viewHolder.tvContentFull.visibility = View.VISIBLE
                viewHolder.tvContent.maxLines = 3
                viewHolder.tvContentFull.text = "Full text"
            } else {
                viewHolder.tvContentFull.visibility = View.GONE
            }

            viewHolder.tvContentFull.setOnClickListener {
                openContent = !openContent
                if (openContent) {
                    viewHolder.tvContent.maxLines = Integer.MAX_VALUE
                    viewHolder.tvContentFull.text = "up"
                } else {
                    viewHolder.tvContent.maxLines = 3
                    viewHolder.tvContentFull.text = "Full text"
                }
            }

            viewHolder.elSquareItem.setOnClickListener {
                activityUtil.go(SquareDetailActivity::class.java).put("id", squareDate.id).start()
            }
            viewHolder.ivHead.setOnClickListener {
                activityUtil.go(TeacherDetailActivity::class.java).put("id", squareDate.teacherId).start()
            }
            viewHolder.tvName.setOnClickListener {
                activityUtil.go(TeacherDetailActivity::class.java).start()
            }
//            viewHolder.tvContent.setOnLongClickListener {
//                //todo 翻译成功后显示
//                viewHolder.tvTranslation.visibility = View.VISIBLE
//                true
//            }
            viewHolder.btnLike.setOnClickListener {
                clickCallback?.doLike(squareDate.id)
            }
            viewHolder.btnComment.setOnClickListener {
                clickCallback.onComment(data, position)
            }
            viewHolder.gvImg.visibility = View.GONE
            if (squareDate.imgUrl != null && squareDate.imgUrl!!.isNotEmpty()) {
                val urls = ArrayList<String>()
                squareDate.imgUrl!!.map {
                    urls.add(it.imgUrl!!)
                }
                viewHolder.gvImg.adapter = SquareImgAdapter(urls, context)
                viewHolder.gvImg.visibility = View.VISIBLE
            }
            viewHolder.llReplay.removeAllViews()
            viewHolder.llReplay.visibility = View.GONE
            if (squareDate.squareCommentList != null && squareDate.squareCommentList!!.isNotEmpty()) {
                viewHolder.llReplay.visibility = View.VISIBLE
                for (i in 0 until squareDate.squareCommentList!!.size) {
                    if (i < 3) {
                        val comment = squareDate.squareCommentList!![i]
                        val tv = TextView(context)
                        if (comment.nickName1 != null) {
                            val tag1 = comment.nickName!!
                            val tag2 = comment.nickName1!!
                            val reply = SpannableString("$tag1 reply $tag2: ${comment.content!!}")
                            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), 0, tag1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            reply.setSpan(StyleSpan(Typeface.BOLD), 0, tag1.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), tag1.length + 6, tag1.length + 8 + tag2.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            reply.setSpan(StyleSpan(Typeface.BOLD), tag1.length + 6, tag1.length + 8 + tag2.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), tag1.length + 1, tag1.length + 6, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            tv.text = reply
                        } else {
                            val tag1 = comment.nickName!!
                            val reply = SpannableString("$tag1: ${comment.content!!.toString().replace(" ", "")}")
                            reply.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.black_00)), 0, tag1.length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            reply.setSpan(StyleSpan(Typeface.BOLD), 0, tag1.length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                            tv.text = reply
                            tv.setOnClickListener {
                                clickCallback.onReply(data, position, comment.id)
                            }
                        }
                        tv.setOnClickListener {
                            clickCallback.onReply(data, position, comment.id)
                        }

                        viewHolder.llReplay.addView(tv)
                    }
                }
                val tv_read_more = TextView(context).apply {
                    text = "Read more>>"
                    setTextColor(context.resources.getColor(R.color.yellow_ffcc00))
                }
                viewHolder.llReplay.addView(tv_read_more)
            }

        }
    }

    fun upDateList(list: List<SquareDate>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    private lateinit var clickCallback: SquareAdapter.ItemClickCallback

    fun setClickCallback(clickCallback: ItemClickCallback) {
        this.clickCallback = clickCallback
    }

    interface ItemClickCallback {
        fun onComment(data: List<SquareDate>, position: Int)
        fun doLike(squareId:Int)
        fun onReply(data: MutableList<SquareDate>, position: Int, id: Int)
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
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
        @BindView(R.id.tv_content_full)
        lateinit var tvContentFull: TextView
        @BindView(R.id.btn_comment)
        lateinit var btnComment: ImageView
        @BindView(R.id.ll_bottom)
        lateinit var llBottom: LinearLayout
        @BindView(R.id.ll_replay)
        lateinit var llReplay: LinearLayout
        @BindView(R.id.el_square_item)
        lateinit var elSquareItem: RelativeLayout

        init {
            ButterKnife.bind(this, view)
        }
    }

}
