package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.xld.foreignteacher.views.StarBarView
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class TeacherEvaluateAdapter(private val context: Context, private val data: List<String>) : BaseAdapter() {
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_evaluate, null)
        }
        val holder = ViewHolder(convertView!!)
        //todo 如果有图片就加载
        val urls = ArrayList<String>()
        urls.add("")
        urls.add("")
        urls.add("")
        urls.add("")
        urls.add("")
        holder.gvImg.adapter = SquareImgAdapter(urls, context)
        holder.gvImg.visibility = View.VISIBLE
        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.sbv_starbar)
        lateinit var sbvStarbar: StarBarView
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: GridView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
