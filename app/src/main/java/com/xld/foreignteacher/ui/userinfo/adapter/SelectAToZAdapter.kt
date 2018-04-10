package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.SelectData
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/5/18.
 */
class SelectAToZAdapter(val context: Context, var list: List<SelectData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val logger = LoggerFactory.getLogger("SelectAToZAdapter")
    private var headView: View? = null

    private var mOnItemClickListener: OnItemClickListener? = null
    override fun getItemCount(): Int {
        return if (headView!=null) {
            list.size + 1
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeadViewHolder){

        }else if (holder is SelectAToZViewHolder){
            val realPosition=getRealPosition(position)
            val section = getSectionForPosition(realPosition)
            when(list[realPosition]){

            }
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (realPosition === getPositionForSection(section)) {
                holder.tvTitle.visibility = View.VISIBLE
                holder.tvTitle.text = list[realPosition].firstLetter()
            } else {
                holder.tvTitle.visibility = View.GONE
            }
            holder.tvLanguage.setOnClickListener { mOnItemClickListener?.onItemClick(list[realPosition]) }
            holder.tvLanguage.text = this.list[realPosition].Name
        }


    }

    private fun getRealPosition(position: Int):Int{
        return if (headView==null){
            position
        }else{
            position-1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if(headView != null && viewType == TYPE_HEADER) return HeadViewHolder(headView!!)
        val view = LayoutInflater.from(context).inflate(R.layout.item_select_a_to_z, parent, false)
        return SelectAToZViewHolder(view)
    }


    fun getPositionForSection(section: Int): Int {
        for (i in 0 until list.size) {

            val sortStr = list[i].firstLetter()
            val firstChar = sortStr!!.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    private fun getSectionForPosition(position: Int): Int {
        return list[position].firstLetter().toCharArray()[0].toInt()
    }

    class SelectAToZViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.ll_item)
        lateinit var llItem: LinearLayout
        @BindView(R.id.tv_language)
        lateinit var tvLanguage: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
    class HeadViewHolder constructor(view: View): RecyclerView.ViewHolder(view) {
        init {
            ButterKnife.bind(this, view)
        }
    }

    fun setHeaderView(headerView: View) {
        this.headView = headerView
        notifyItemInserted(0)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            headView==null -> TYPE_NORMAL
            position==0 -> TYPE_HEADER
            else -> TYPE_NORMAL
        }
    }

    interface OnItemClickListener {
        fun onItemClick(selectData: SelectData)
    }

    fun updateList(list: List<SelectData>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_NORMAL = 2
    }

}
