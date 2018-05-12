package com.whynuttalk.foreignteacher.ui.mine.verification.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.AuthenticationDetail
import com.whynuttalk.foreignteacher.ui.mine.verification.UpLoadIdActivity

/**
 * Created by cz on 5/10/18.
 */
class TeacherCertificateAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is TeacherCertificateViewHolder) {
            holder.tvAttestation.text = list[position].certificateName + " attestation"
        } else if (holder is FootViewHolder) {
            holder.tvAdd.setOnClickListener {ActivityUtil.create(context).go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.CERTIFICATE).start() }
        }
    }

    var list = mutableListOf<AuthenticationDetail>()
    override fun getItemCount(): Int {
        return list.size + 1
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOT) {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_add, parent, false)
            FootViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_teaching_certificate, parent, false)
            TeacherCertificateViewHolder(view)
        }
    }

    class TeacherCertificateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_attestation)
        lateinit var tvAttestation: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

    class FootViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_add)
        lateinit var tvAdd: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == (list.size)) {
            TYPE_FOOT
        } else {
            TYPE_NORMAL
        }
    }


    fun setData(it: List<AuthenticationDetail>) {
        list.clear()
        list.addAll(it)
        notifyDataSetChanged()
    }

    companion object {
        const val TYPE_FOOT = 1
        const val TYPE_NORMAL = 2

    }
}
