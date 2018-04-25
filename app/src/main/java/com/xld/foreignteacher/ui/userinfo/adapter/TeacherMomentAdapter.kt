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
import cn.sinata.xldutils.utils.TimeUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherDetail
import com.xld.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.xld.foreignteacher.views.NestedGridView
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class TeacherMomentAdapter(private val context: Context) : BaseAdapter() {
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val dataList = mutableListOf<TeacherDetail.SquareListBean>()

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
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
        holder.tvContent.text = dataList[position].content
        holder.tvTime.text = TimeUtils.getTimeHM(dataList[position].createTime)
        holder.tvContent!!.setOnLongClickListener {
            //TODO 翻译成功后显示
//            holder.tvTranslation!!.visibility = View.VISIBLE
//            holder.tvTranslation!!.text = "翻译结果"
            true
        }
        val urls = ArrayList<String>()

        if (dataList[position].imgList != null && dataList[position].imgList!!.isNotEmpty()) {
            dataList[position].imgList!!.sortedBy { it.sort }.map {
                urls.add(it.imgUrl!!)
            }
            holder.gvImg.adapter = SquareImgAdapter(urls, context)
            holder.gvImg.visibility = View.VISIBLE
        }
        return convertView
    }

    fun upDataList(list: List<TeacherDetail.SquareListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.tv_content)
        lateinit var tvContent: TextView
//        @BindView(R.id.tv_translation)
//        lateinit var tvTranslation: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.gv_img)
        lateinit var gvImg: NestedGridView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
