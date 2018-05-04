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
import cn.sinata.xldutils.utils.TimeUtils
import cn.sinata.xldutils.utils.Utils
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.PersonalTrainingOrder
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.order.single.OrderDetailActivity
import com.xld.foreignteacher.ui.order.single.PendingDetailActivity
import com.xld.foreignteacher.ui.order.single.RateActivity
import com.xld.foreignteacher.ui.order.single.SingleOrderFragment
import com.xld.foreignteacher.ui.report.DeclinedActivity
import com.xld.foreignteacher.ui.report.ReportActivity
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/3/18.
 */
class SingleOrderAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val type: String) : LoadMoreAdapter() {
    private val data = mutableListOf<PersonalTrainingOrder.RowsBean>()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("SquareAdapter")
    private lateinit var singleOrderItemClickListener: SingleOrderItemClickListener

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

    fun setDataList(list: List<PersonalTrainingOrder.RowsBean>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            val order = data[position]
            val viewHolder = holder as ViewHolder

            if (order.user != null && order.user!!.imgUrl != null) {
                viewHolder.ivHead.setImageURI(order.user!!.imgUrl)
            }
            viewHolder.tvName.text = order.user!!.nickName
            val age = TimeUtils.getAge(order.user!!.birthDay) - 1
            viewHolder.tvAge.text = String.format(context.resources.getString(R.string.ages), age)
            viewHolder.tvTitle.text = order.curriculum?.title ?: ""
            viewHolder.tvWeeks.text = order.bookingAutoWeeks.toString() + " Weeks"
            viewHolder.tvInfo.text = order.curriculum?.className ?: ""

            var backDra = if (order.user!!.sex == 1) {
                context.resources.getDrawable(R.mipmap.icon_male)
            } else {
                context.resources.getDrawable(R.mipmap.icon_woman)
            }
            backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)

            viewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
            viewHolder.tvName.compoundDrawablePadding = backDra.minimumHeight / 2
            viewHolder.tvAttendingClient.text = String.format(context.resources.getString(R.string.attending_client), order.numberOfPeople)
            viewHolder.tvLocation.text = order.address
            viewHolder.tvPrice.text = "￥" + order.payMoney
            val week = TimeUtils.getCurrentTimeMMDD(order.curriculum?.startTime ?: 0)
            val start = TimeUtils.getTimeHM(order.curriculum?.startTime ?: 0)
            val end = TimeUtils.getTimeHM(order.curriculum?.endTime ?: 0)
            viewHolder.tvClassTime.text = "$week $start ~ $end"
            viewHolder.tvAttendingClient.text = String.format(context.resources.getString(R.string.attending_client), order.numberOfPeople)
            viewHolder.imgLocate.setOnClickListener {
//                AmapNaviPage.getInstance().showRouteActivity(context, AmapNaviParams(null,null, Poi(order.address,null,null), AmapNaviType.DRIVER)
//                        , object :INaviInfoCallback{})
            }
            when (type) {
                SingleOrderFragment.NEW_ORDERS -> {
                    viewHolder.tvPromotion.visibility = View.VISIBLE
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        activityUtil.go(DeclinedActivity::class.java).start()
                    }

                    viewHolder.btnAccept.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.comfire_and_accept_order))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type)
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "accept")
                    }
                }
                SingleOrderFragment.PENDING -> {
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(PendingDetailActivity::class.java).start()
                    }
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

                }
                SingleOrderFragment.FINISHED -> {
                    viewHolder.tvAccept.visibility = View.GONE
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnAccept.text = "Rate"
                    viewHolder.btnAccept.setOnClickListener {
                        activityUtil.go(RateActivity::class.java).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type)
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "delete")
                    }
                }
                SingleOrderFragment.CANCELED -> {
                    viewHolder.tvAccept.text = "Me-Cancellation Reasons"
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.tvCancelReason.visibility = View.VISIBLE
                    viewHolder.tvCancelReason.text = order.userCancelReason ?: order.teacherCancelReason
                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnAccept.visibility = View.GONE
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type)
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "delete")
                    }

                }
                SingleOrderFragment.DECLINED -> {
                    viewHolder.tvAccept.text = "Refusal cause"
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.tvCancelReason.visibility = View.VISIBLE
                    viewHolder.tvCancelReason.text = order.refuseReason
                    viewHolder.btnAccept.visibility = View.GONE
                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type)
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "delete")
                    }
                }

            }


        }
    }

    fun setListener(listener: SingleOrderItemClickListener) {
        singleOrderItemClickListener = listener
    }

    interface SingleOrderItemClickListener {
        fun onDialogClick(type: String)
    }


    inner class ViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.iv_head)
        lateinit var ivHead: SimpleDraweeView
        @BindView(R.id.img_locate)
        lateinit var imgLocate: ImageView
        @BindView(R.id.tv_name)
        lateinit var tvName: TextView
        @BindView(R.id.tv_age)
        lateinit var tvAge: TextView
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
        @BindView(R.id.tv_promotion)
        lateinit var tvPromotion: TextView


        init {
            ButterKnife.bind(this, view)
        }
    }
}
