package com.xld.foreignteacher.ui.mine.wallet

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/13/18.
 */
class TransactionsAdapter(val context: Context): LoadMoreAdapter() {
    private val data: MutableList<String>
    private val activityUtil: ActivityUtil
    private val logger = LoggerFactory.getLogger("SquareAdapter")

    init {
        data = ArrayList()
        activityUtil = ActivityUtil.create(context)
        data.add("")
        data.add("")
        data.add("")
        data.add("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_with_draw_detail, parent, false)
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
        return super.getItemCount() + data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LoadMoreAdapter.TYPE_NORMAL) {

        }
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
//        @BindView(R.id.iv_head)
//        lateinit var ivHead: SimpleDraweeView
//        @BindView(R.id.tv_name)
//        lateinit var tvName: TextView
//        @BindView(R.id.tv_time)
//        lateinit var tvTime: TextView
//        @BindView(R.id.tv_content)
//        lateinit var tvContent: TextView
//        @BindView(R.id.tv_translation)
//        lateinit var tvTranslation: TextView
//        @BindView(R.id.gv_img)
//        lateinit var gvImg: NestedGridView
//        @BindView(R.id.tv_location)
//        lateinit var tvLocation: TextView
//        @BindView(R.id.btn_like)
//        lateinit var btnLike: TextView
//        @BindView(R.id.btn_comment)
//        lateinit var btnComment: ImageView
//        @BindView(R.id.ll_bottom)
//        lateinit var llBottom: LinearLayout
//        @BindView(R.id.ll_replay)
//        lateinit var llReplay: LinearLayout
//        @BindView(R.id.el_square_item)
//        lateinit var elSquareItem: RelativeLayout

        init {
            ButterKnife.bind(this, view)
        }
    }
}