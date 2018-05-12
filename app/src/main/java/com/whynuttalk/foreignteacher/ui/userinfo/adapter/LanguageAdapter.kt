package com.whynuttalk.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.Language

/**
 * Created by cz on 4/8/18.
 */
class LanguageAdapter(val context: Context, var list: List<Language>, var isEditable: Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var showFoot = true
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is LanguageViewHolder) {
            holder.tvLanguage.text = "Language-${CHARS[position + 1]}"
            holder.tvLanguageEdit.text = list[position].eName
            if (isEditable) {
                holder.img_delete.visibility = View.VISIBLE
            } else {
                holder.img_delete.visibility = View.GONE
            }
            holder.img_delete.setOnClickListener {
                mOnItemClickListener?.onDeleteClick(list[position])
            }
        } else if (holder is AddLanguageViewHolder) {
            holder.tvAddLanguage.setOnClickListener {
                mOnItemClickListener?.onAddItemClick()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (showFoot) {
            list.size + 1
        } else {
            list.size
        }
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

    fun isShowFoot(boolean: Boolean) {
        showFoot = boolean
    }

    class LanguageViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_language)
        lateinit var tvLanguage: TextView
        @BindView(R.id.tv_language_edit)
        lateinit var tvLanguageEdit: TextView
        @BindView(R.id.img_delete)
        lateinit var img_delete: ImageView

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
        fun onDeleteClick(language: Language)
    }

    companion object {
        const val TYPE_FOOT = 1
        const val TYPE_NORMAL = 2
        private val CHARS = Array(26, {
            ('A' + it - 1).toString()
        })
    }
}
