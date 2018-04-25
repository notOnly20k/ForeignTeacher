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
import com.xld.foreignteacher.api.dto.TeacherDetail
import com.xld.foreignteacher.ui.square.adapter.SquareImgAdapter
import com.xld.foreignteacher.views.StarBarView
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class TeacherEvaluateAdapter(private val context: Context) : BaseAdapter() {
    private val logger =LoggerFactory.getLogger("TeacherEvaluateAdapter")
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val dataList = mutableListOf<TeacherDetail.CommentListBean>()
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_teacher_evaluate, null)
        }
        val holder = ViewHolder(convertView!!)
        holder.ivHead.setImageURI(dataList[position].userImgUrl)
        holder.sbvStarbar.setStarRating(dataList[position].score.toFloat())
        holder.tvContent.text = dataList[position].remark?:""
        //todo 如果有图片就加载
        val urls = ArrayList<String>()
        if (dataList[position].imgUrl != null && dataList[position].imgUrl!!.isNotEmpty()) {
            dataList[position].imgUrl!!.sortedBy { it.sort }.map {
                urls.add(it.imgUrl!!)
            }
            holder.gvImg.adapter = SquareImgAdapter(urls, context)
            holder.gvImg.visibility = View.VISIBLE
        }else{
            holder.gvImg.visibility = View.GONE
        }
        return convertView
    }

    fun upDataList(list: List<TeacherDetail.CommentListBean>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
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
