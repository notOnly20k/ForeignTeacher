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
import com.xld.foreignteacher.api.dto.Language
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/5/18.
 */
class SelectAToZAdapter(val context: Context, var list: List<Language>) : RecyclerView.Adapter<SelectAToZAdapter.SelectAToZViewHolder>() {
    val logger = LoggerFactory.getLogger("SelectAToZAdapter")

    private var mOnItemClickListener: OnItemClickListener? = null
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SelectAToZViewHolder, position: Int) {
        val section = getSectionForPosition(position)
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position === getPositionForSection(section)) {
            holder.tvTitle.visibility = View.VISIBLE
            holder.tvTitle.text = list[position].firstLetter()
        } else {
            holder.tvTitle.visibility = View.GONE
        }
        holder.tvLanguage.setOnClickListener { mOnItemClickListener?.onItemClick(list[position]) }
        holder.tvLanguage.text = this.list[position].eName


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SelectAToZViewHolder {
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

    fun getSectionForPosition(position: Int): Int {
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

    interface OnItemClickListener {
        fun onItemClick(language: Language)
    }

    fun updateList(list: List<Language>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

}
