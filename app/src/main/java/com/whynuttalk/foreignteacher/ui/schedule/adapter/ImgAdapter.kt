package com.whynuttalk.foreignteacher.ui.schedule.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.drawee.view.SimpleDraweeView
import com.whynuttalk.foreignteacher.R
import java.io.File

/**
 * Created by cz on 5/7/18.
 */
class ImgAdapter(val list: List<String>): RecyclerView.Adapter<ImgAdapter.ImgViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImgViewHolder {
      val view=LayoutInflater.from(parent.context).inflate(R.layout.item_pic_match,parent,false)
        return ImgViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImgViewHolder?, position: Int) {
       holder!!.imgPic.setImageURI(Uri.fromFile(File(list[position])))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ImgViewHolder(view: View):RecyclerView.ViewHolder(view){
        @BindView(R.id.img_pic)
        lateinit var imgPic: SimpleDraweeView

        init {
            ButterKnife.bind(this, view)
        }
    }
}