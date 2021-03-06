package com.whynuttalk.foreignteacher.ui.square.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.views.SquareDraweeView

/**
 * Created by cz on 4/1/18.
 */
class SquareImgAdapter(private val data: List<String>, private val context: Context) : BaseAdapter() {

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_square_img, null)
        }
        val draweeView = convertView as SquareDraweeView?
        draweeView?.setImageURI(data[position])
        //todo 渲染
        return convertView!!
    }
}