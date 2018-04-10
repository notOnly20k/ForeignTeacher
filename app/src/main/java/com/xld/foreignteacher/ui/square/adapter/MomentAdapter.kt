package com.xld.foreignteacher.ui.square.adapter

import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.dialog.CustomDialog
import java.io.File

/**
 * Created by cz on 4/9/18.
 */
class MomentAdapter(val context: Context, var list: List<String>, val fragmentManager: FragmentManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is PicViewHolder) {
            holder.imgPic.setImageURI(Uri.fromFile(File(list[position])))
            holder.imgPic.setOnClickListener {
                CustomDialog.Builder()
                        .create()
                        .showtitle(false)
                        .showButton2(false)
                        .setButton1Text((R.string.delete).toFormattedString(context))
                        .setDialogListene(object : CustomDialog.CustomDialogListener {
                            override fun clickButton1(customDialog: CustomDialog) {
                                mOnItemClickListener!!.delete(list[position], position)
                                customDialog.dismiss()
                            }

                            override fun clickButton2(customDialog: CustomDialog) {
                            }

                        }).show(fragmentManager, "delete_photo")
            }

        } else if (holder is AddPicViewHolder) {
            holder.imgPic.setOnClickListener {
                CustomDialog.Builder()
                        .create()
                        .showtitle(false)
                        .setButton1Text((R.string.take_photo).toFormattedString(context))
                        .setButton2Text((R.string.select_image).toFormattedString(context))
                        .setDialogListene(object : CustomDialog.CustomDialogListener {
                            override fun clickButton1(customDialog: CustomDialog) {
                                mOnItemClickListener!!.takePhoto()
                                customDialog.dismiss()
                            }

                            override fun clickButton2(customDialog: CustomDialog) {
                                mOnItemClickListener!!.select_image()
                                customDialog.dismiss()
                            }

                        }).show(fragmentManager, "select_photo")
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list.size == 9) {
            list.size
        } else {
            list.size + 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_FOOT) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pic, parent, false)
            AddPicViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pic, parent, false)
            PicViewHolder(view)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (list.size)) {
            TYPE_FOOT
        } else {
            TYPE_NORMAL
        }
    }

    class PicViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.img_pic)
        lateinit var imgPic: SimpleDraweeView

        init {
            ButterKnife.bind(this, view)
        }
    }

    class AddPicViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.img_pic)
        lateinit var imgPic: SimpleDraweeView

        init {
            ButterKnife.bind(this, view)
        }
    }

    fun updateList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    interface OnItemClickListener {
        fun takePhoto()
        fun select_image()
        fun delete(url: String, position: Int)
    }

    companion object {
        const val TYPE_FOOT = 1
        const val TYPE_NORMAL = 2
    }
}
