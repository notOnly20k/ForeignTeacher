package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.Language

/**
 * Created by cz on 4/8/18.
 */
class LanguageAdapter(val context: Context, var list: List<Language>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is LanguageViewHolder) {
            holder.tvLanguage.text = "Language-${CHARS[position+1]}"
            holder.tvLanguageEdit.text = list[position].eName
        } else if (holder is AddLanguageViewHolder) {
            holder.tvAddLanguage.setOnClickListener {
                mOnItemClickListener?.onAddItemClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOT) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_add_languages, parent, false)
            AddLanguageViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false)
            LanguageViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (list.size)) {
            TYPE_FOOT
        } else {
            TYPE_NORMAL
        }
    }

    class LanguageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_language)
        lateinit var tvLanguage: TextView
        @BindView(R.id.tv_language_edit)
        lateinit var tvLanguageEdit: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

    class AddLanguageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_add_language)
        lateinit var tvAddLanguage: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

    fun updateList(list: List<Language>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    interface OnItemClickListener {
        fun onAddItemClick()
    }

    companion object {
        const val TYPE_FOOT = 1
        const val TYPE_NORMAL = 2
        private val CHARS = Array(26, {
                ('A' + it - 1).toString()
        })
    }
}
