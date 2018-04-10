package com.xld.foreignteacher.ui.order.adapter

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.order.group.GroupOrdersFragment
import com.xld.foreignteacher.views.RoundBackgroundColorSpan
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/8/18.
 */
class GroupOrderAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val type: String) : LoadMoreAdapter() {

    private val activityUtil: ActivityUtil
    private val logger = LoggerFactory.getLogger("GroupOrderAdapter")
    private val data: MutableList<String>
    private var callback: OnItemClickCallback? = null

    private val countDownMap: SparseArray<CountDownTimer>?

    init {
        countDownMap = SparseArray()
        data = ArrayList()
        activityUtil = ActivityUtil.create(context)
        data.add("")
        data.add("")
        data.add("")
        data.add("")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            holder as GroupOrderViewHolder
            val tag ="English"
            val title = SpannableString(tag + "   What do you think of Christmas? hahhahahaahahahahaha")
            title.setSpan(RoundBackgroundColorSpan(context, Color.parseColor("#FCFFE0"), context.resources.getColor(R.color.yellow_ffcc00), 13), 0, tag.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            holder.tvPinpinTile.text=title
            when (type) {
                GroupOrdersFragment.FOR_SALE -> {
                    val backDra = context.resources.getDrawable(R.mipmap.icon_like)
                    backDra.setBounds(0, 0, backDra.minimumHeight, backDra.minimumHeight)
                    holder.tvDelete.visibility = View.GONE
                    holder.tvPinpinPersonCount.setCompoundDrawables(backDra,null,null,null)
                    holder.tvPinpinPersonCount.text="13"
                }
                GroupOrdersFragment.OPEN -> {
                    holder.tvDelete.visibility = View.GONE
                }
                GroupOrdersFragment.PENDING -> {
                    holder.tvDelete.visibility = View.GONE
                }
                GroupOrdersFragment.FINISHED -> {
                    holder.tvDelete.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle("Are you sure you want to delete this record?")
                                .setButton1Text("Yes")
                                .setButton2Text("No")
                                .setDialogListene(object :CustomDialog.CustomDialogListener{
                                    override fun clickButton1(customDialog: CustomDialog) {

                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                       customDialog.dismiss()
                                    }

                                }).show(fragmentManager,"delete_order")
                    }
                }
                GroupOrdersFragment.CANCELED -> {
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_group_order, parent, false)
            return GroupOrderViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            TYPE_FOOTER
        else
            TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + data.size
    }

    inner class GroupOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_pinpin_img)
        lateinit var ivPinpinImg: SimpleDraweeView
        @BindView(R.id.tv_pinpin_price)
        lateinit var tvPinpinPrice: TextView
        @BindView(R.id.tv_pinpin_person_count)
        lateinit var tvPinpinPersonCount: TextView
        @BindView(R.id.tv_timer)
        lateinit var tvTimer: TextView
        @BindView(R.id.tv_pinpin_tile)
        lateinit var tvPinpinTile: TextView
        @BindView(R.id.tv_datetime)
        lateinit var tvDatetime: TextView
        @BindView(R.id.tv_location)
        lateinit var tvLocation: TextView
        @BindView(R.id.tv_delete)
        lateinit var tvDelete: TextView

        var countDownTimer: CountDownTimer? = null

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                if (callback != null)
                    callback!!.onItemClick(data[adapterPosition])
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(id: String)
    }


    fun setCallback(callback: OnItemClickCallback) {
        this.callback = callback
    }


}
