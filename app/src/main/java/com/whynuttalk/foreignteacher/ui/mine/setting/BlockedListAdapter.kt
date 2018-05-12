package com.whynuttalk.foreignteacher.ui.mine.setting

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.BlockUser
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/13/18.
 */
class BlockedListAdapter(val context: Context) : LoadMoreAdapter() {
    private val data: MutableList<BlockUser> = mutableListOf()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("SquareAdapter")
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == LoadMoreAdapter.TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_blocked_list, parent, false)
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
            val blockUser = data[position]
            holder as ViewHolder
            val sex = if (blockUser.sex == 1) {
                R.mipmap.icon_male
            } else {
                R.mipmap.icon_female
            }
            holder.tvName.text=blockUser.nickName
            holder.imgSex.setImageDrawable(context.resources.getDrawable(sex))
            holder.ivHead.setImageURI(blockUser.imgUrl)
            holder.btnDelete.setOnClickListener {

                onItemClickListener?.delet(blockUser.id,holder.getAdapterPosition())
            }

        }
    }

    fun removeData(position: Int){
        data.removeAt(position)
    }

    interface OnItemClickListener {
        fun delet(id: Int,position: Int)
    }

    fun setListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun upDateList(list: List<BlockUser>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.img_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.img_sex)
        lateinit var imgSex: ImageView
        @BindView(R.id.btnDelete)
        lateinit var btnDelete: Button


        init {
            ButterKnife.bind(this, view)
        }
    }
}
