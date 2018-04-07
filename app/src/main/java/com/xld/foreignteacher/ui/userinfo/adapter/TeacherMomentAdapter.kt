package com.xld.foreignteacher.ui.userinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.square.SquareImgAdapter
import com.xld.foreignteacher.views.NestedGridView
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class TeacherMomentAdapter(private val context: Context, private val data: List<String>) : BaseAdapter() {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_moment, null)
        }
        val holder = ViewHolder(convertView!!)
        holder.tvContent!!.setOnLongClickListener {
            //TODO 翻译成功后显示
            holder.tvTranslation!!.visibility = View.VISIBLE
            holder.tvTranslation!!.text = "翻译结果"
            true
        }
        //todo 如果有图片就加载
        val urls = ArrayList<String>()
        urls.add("")
        urls.add("")
//        urls.add("")
//        urls.add("")
//        urls.add("")
        holder.gvImg.adapter = SquareImgAdapter(urls, context)
        holder.gvImg.visibility = View.VISIBLE
        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
        @BindView(R.id.tv_translation)
        lateinit var tvTranslation: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: NestedGridView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
