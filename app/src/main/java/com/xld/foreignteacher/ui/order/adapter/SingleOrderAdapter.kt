package com.xld.foreignteacher.ui.order.adapter

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.Utils
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.order.single.OrderDetailActivity
import com.xld.foreignteacher.ui.order.single.SingleOrderFragment
import com.xld.foreignteacher.ui.report.DeclinedActivity
import com.xld.foreignteacher.ui.report.ReportActivity
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 4/3/18.
 */
class SingleOrderAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val type: String) : LoadMoreAdapter() {
    private val data: MutableList<String>
    private val activityUtil: ActivityUtil
    private val logger = LoggerFactory.getLogger("SquareAdapter")

    init {
        data = ArrayList()
        activityUtil = ActivityUtil.create(context)
        data.add("")
        data.add("")
        data.add("")
        data.add("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_single_order, parent, false)
            return ViewHolder(view)
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val viewHolder = holder as ViewHolder
            when (type) {
                SingleOrderFragment.NEW_ORDERS -> {
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).start()
                    }

                    viewHolder.btnAccept.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.comfire_and_accept_order))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "accept")
                    }
                }
                SingleOrderFragment.PENDING -> {
                    viewHolder.tvAccept.visibility = View.GONE
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.imgCall.visibility = View.VISIBLE
                    viewHolder.imgCall.setOnClickListener {
                        Utils.callPhone(context, "110")
                    }
                    viewHolder.btnCancel.text = "Cancel request"
                    viewHolder.btnCancel.setOnClickListener {
                        activityUtil.go(ReportActivity::class.java).put("id", 0).put("type", ReportActivity.CANCEL_REQUEST).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        activityUtil.go(DeclinedActivity::class.java).start()
                    }

                }
                SingleOrderFragment.FINISHED -> {
                    viewHolder.tvAccept.visibility = View.GONE
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnAccept.text = "Rate"
                }
                SingleOrderFragment.CANCELED -> {
                    viewHolder.tvAttendingClient.text = "Me-Cancellation Reasons"
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.tvCancelReason.visibility = View.VISIBLE
                    viewHolder.btnCancel.text = "Delete"
                }
                SingleOrderFragment.DECLINED -> {
                    viewHolder.tvAccept.text = "Refusal cause"
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.tvCancelReason.visibility = View.VISIBLE
                    viewHolder.btnCancel.text = "Delete"
                }

            }


        }
    }

    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_time)
        lateinit var tvTime: TextView
        @BindView(R.id.tv_price)
        lateinit var tvPrice: TextView
        @BindView(R.id.view)
        lateinit var view: View
        @BindView(R.id.tv_weeks)
        lateinit var tvWeeks: TextView
        @BindView(R.id.tv_title)
        lateinit var tvTitle: TextView
        @BindView(R.id.tv_info)
        lateinit var tvInfo: TextView
        @BindView(R.id.tv_attending_client)
        lateinit var tvAttendingClient: TextView
        @BindView(R.id.tv_class_time)
        lateinit var tvClassTime: TextView
        @BindView(R.id.tv_location)
        lateinit var tvLocation: TextView
        @BindView(R.id.view1)
        lateinit var view1: View
        @BindView(R.id.tv_accept)
        lateinit var tvAccept: TextView
        @BindView(R.id.tv_accept_count)
        lateinit var tvAcceptCount: TextView
        @BindView(R.id.tv_cancel_reason)
        lateinit var tvCancelReason: TextView
        @BindView(R.id.img_call)
        lateinit var imgCall: ImageView
        @BindView(R.id.btn_accept)
        lateinit var btnAccept: TextView
        @BindView(R.id.btn_cancel)
        lateinit var btnCancel: TextView
        @BindView(R.id.rl_title)
        lateinit var rlTitle: LinearLayout
        @BindView(R.id.cv_item)
        lateinit var cvItem: CardView


        init {
            ButterKnife.bind(this, view)
        }
    }
}
