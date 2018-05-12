package com.whynuttalk.foreignteacher.ui.order.adapter

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.utils.ActivityUtil
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.TrainingChildOrder
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.order.single.PendingDetailActivity
import com.whynuttalk.foreignteacher.ui.order.single.RateActivity
import com.whynuttalk.foreignteacher.ui.report.ReportActivity
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 4/15/18.
 */
class PendingDetailAdapter(val context: Context, val type: String, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<PendingDetailAdapter.PendingViewHold>() {
    var data = TrainingChildOrder()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("PendingDetailAdapter")
    private lateinit var pendingItemClickListener: PendingItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PendingViewHold {
        val view = LayoutInflater.from(context).inflate(R.layout.item_detail_pending_order, parent, false)
        return PendingViewHold(view)
    }

    fun setListener(listener: PendingItemClickListener) {
        pendingItemClickListener = listener
    }

    override fun onBindViewHolder(holder: PendingViewHold, position: Int) {
        val formart = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        when (type) {
            PendingDetailActivity.FINISH -> {
                holder.tvInfo.visibility = View.GONE
                holder.tvReason.visibility = View.GONE
                val order = data.FinishBookingUsers!![position]

                holder.tvDate.text = formart.format(order.day)+getTime(order.startTime,order.endTime)
                holder.btnRate.visibility = View.VISIBLE
                holder.btnRate.setOnClickListener {
                    activityUtil.go(RateActivity::class.java).put("id", order.id).start()
                }
                holder.btnDelete.setOnClickListener {
                    CustomDialog.Builder()
                            .create()
                            .setTitle(context.getString(R.string.delete_record))
                            .setDialogListene(object : CustomDialog.CustomDialogListener {
                                override fun clickButton1(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                    pendingItemClickListener.onDialogClick(type, order.id)
                                }

                                override fun clickButton2(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                }

                            })
                            .show(fragmentManager, "delete")

                }
            }
            PendingDetailActivity.PENDING -> {
                holder.tvInfo.visibility = View.GONE
                holder.tvReason.visibility = View.GONE
                val order = data.UnFinishBookingUsers!![position]
                holder.tvDate.text = formart.format(order.day)+getTime(order.startTime,order.endTime)
                holder.btnRate.visibility = View.GONE
                holder.btnDelete.text = "Cancel Request"
                holder.btnDelete.setOnClickListener {
                    activityUtil.go(ReportActivity::class.java).put("id", data.orderInfo!!.id).put("type", ReportActivity.CANCEL_MAIN_REQUEST)
                            .put("bookingTeacherId", order.id).start()
                }
            }
            PendingDetailActivity.CANCEL -> {
                val order = data.CanceledBookingUsers!![position]
                holder.tvDate.text = formart.format(order.day)+getTime(order.startTime,order.endTime)
                holder.tvInfo.visibility = View.VISIBLE
                holder.tvReason.visibility = View.VISIBLE
                if (order.state == 1) {
                    holder.tvInfo.text = "Me-Cancellation Reason"
                } else {
                    holder.tvInfo.text = "Cancellation Reason"
                }
                holder.btnDelete.setOnClickListener {
                    CustomDialog.Builder()
                            .create()
                            .setTitle(context.getString(R.string.delete_record))
                            .setDialogListene(object : CustomDialog.CustomDialogListener {
                                override fun clickButton1(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                    pendingItemClickListener.onDialogClick(type, order.id)
                                }

                                override fun clickButton2(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                }

                            })
                            .show(fragmentManager, "delete")
                }

                holder.tvReason.text = order.refuseReason
                holder.btnRate.visibility = View.GONE
            }
        }
    }

    fun getTime(t1:Double,t2:Double): String {
        val startTime = t1.toString().split(".")
        val endTime =t2.toString().split(".")
        var halfStartTime = "00"
        var halfEndTime = "00"
        if (startTime[1] == "5") {
            halfStartTime = "30"
        }
        if (endTime[1] == "5") {
            halfEndTime = "30"
        }

        return " ${startTime[0]}:$halfStartTime-${endTime[0]}:$halfEndTime"
    }

    fun addData(data:TrainingChildOrder){
        this.data=data
        notifyDataSetChanged()
    }

    interface PendingItemClickListener {
        fun onDialogClick(type: String, id: Int)
    }

    override fun getItemCount(): Int {
        return when (type) {
            PendingDetailActivity.FINISH -> {
                data.FinishBookingUsers?.size ?: 0
            }
            PendingDetailActivity.PENDING -> {
                data.UnFinishBookingUsers?.size ?: 0
            }
            PendingDetailActivity.CANCEL -> {
                data.CanceledBookingUsers?.size ?: 0
            }
            else -> {
                0
            }
        }

    }

    class PendingViewHold(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_date)
        lateinit var tvDate: TextView
        @BindView(R.id.tv_info)
        lateinit var tvInfo: TextView
        @BindView(R.id.tv_reason)
        lateinit var tvReason: TextView
        @BindView(R.id.btn_delete)
        lateinit var btnDelete: TextView
        @BindView(R.id.btn_rate)
        lateinit var btnRate: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }
}
