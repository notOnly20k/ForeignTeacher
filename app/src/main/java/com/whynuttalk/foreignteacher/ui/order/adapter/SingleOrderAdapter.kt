package com.whynuttalk.foreignteacher.ui.order.adapter

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
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
import com.hyphenate.easeui.EaseConstant
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.PersonalTrainingOrder
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.msg.ChatActivity
import com.whynuttalk.foreignteacher.ui.order.single.OrderDetailActivity
import com.whynuttalk.foreignteacher.ui.order.single.PendingDetailActivity
import com.whynuttalk.foreignteacher.ui.order.single.RateActivity
import com.whynuttalk.foreignteacher.ui.order.single.SingleOrderFragment
import com.whynuttalk.foreignteacher.ui.report.DeclinedActivity
import com.whynuttalk.foreignteacher.ui.report.ReportActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by cz on 4/3/18.
 */
class SingleOrderAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val type: String) : LoadMoreAdapter() {
    private val data = mutableListOf<PersonalTrainingOrder.RowsBean>()
    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("SquareAdapter")
    private val disposableList: MutableList<Disposable> = mutableListOf()
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
            viewHolder.tvAge.text = String.format(context.resources.getString(R.string.ages), order.user!!.age)
            viewHolder.tvTitle.text = order.curriculum?.title ?: ""
            if (order.bookingAutoWeeks != 0) {
                viewHolder.tvWeeks.text = order.bookingAutoWeeks.toString() + " Weeks"
            } else {
                viewHolder.tvWeeks.visibility = View.GONE
            }
            viewHolder.tvInfo.text = order.curriculum?.className

            var backDra = if (order.user!!.sex == 1) {
                context.resources.getDrawable(R.mipmap.icon_male)
            } else {
                context.resources.getDrawable(R.mipmap.icon_woman)
            }
            backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)
            viewHolder.imgCall.setOnClickListener { Utils.callPhone(context, order.phone) }
            viewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
            viewHolder.tvName.compoundDrawablePadding = backDra.minimumHeight / 2
            viewHolder.tvAttendingClient.text = String.format(context.resources.getString(R.string.attending_client), order.numberOfPeople)
            viewHolder.tvLocation.text = order.address
            viewHolder.tvPrice.text = context.getString(R.string.price_class, order.curriculum!!.price.toInt())
            val week = TimeUtils.getCurrentTimeMMDD(order.curriculum?.startTime ?: 0)
            val start = TimeUtils.getTimeHM(order.curriculum?.startTime ?: 0)
            val end = TimeUtils.getTimeHM(order.curriculum?.endTime ?: 0)
            viewHolder.tvClassTime.text = "$week $start ~ $end"
            viewHolder.tvAttendingClient.text = String.format(context.resources.getString(R.string.attending_client), order.numberOfPeople)
            viewHolder.imgLocate.setOnClickListener { singleOrderItemClickListener.loacte(order.address ?: "") }


            when (type) {
                SingleOrderFragment.NEW_ORDERS -> {
                    viewHolder.tvPromotion.visibility = View.VISIBLE
                    viewHolder.tvPromotion.text = "Prompt you to accept the order: ${order.reminderCount} times"
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).put("id", order.id).put("type", SingleOrderFragment.NEW_ORDERS).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        activityUtil.go(DeclinedActivity::class.java).put("id", order.id).start()
                    }

                    Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                            .doOnSubscribe {
                                holder.disposable.add(it)
                                disposableList.add(it)
                            }
                            .map {
                                val formart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                val calend = Calendar.getInstance()
                                calend.time = Date(order.addtime)
                                calend.set(Calendar.HOUR, calend.get(Calendar.HOUR) + 1)
                                val time = TimeUtils.parseTimeMillisecond(formart.format(calend.time))
                                if (time - System.currentTimeMillis() > 0) {
                                    DateUtils.formatElapsedTime((time - System.currentTimeMillis()) / 1000L)
                                } else {
                                    "00:00:00"
                                }
                            }
                            .subscribe {
                                holder.tvAcceptCount.text = "$it"
                                if (it == "00:00:00") {
                                    holder.disposable.dispose()
                                }
                            }

                    viewHolder.btnAccept.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.comfire_and_accept_order))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type, order.id)
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
                        activityUtil.go(PendingDetailActivity::class.java).put("id", order.id).start()
                    }
                    viewHolder.tvAccept.visibility = View.GONE
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.imgCall.visibility = View.VISIBLE
                    viewHolder.btnCancel.text = "Cancel request"
                    viewHolder.btnAccept.text = "Chat"
                    viewHolder.btnCancel.setOnClickListener {
                        activityUtil.go(ReportActivity::class.java).put("id", order.id).put("type", ReportActivity.CANCEL_MAIN_REQUEST).start()
                    }
                    viewHolder.btnAccept.setOnClickListener {
                        activityUtil.go(ChatActivity::class.java).put(EaseConstant.EXTRA_USER_ID, "waijiao_client_" + order.user!!.id).start()
                    }

                }
                SingleOrderFragment.FINISHED -> {
                    viewHolder.tvAccept.visibility = View.GONE
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnAccept.text = "Rate"
                    viewHolder.btnAccept.setOnClickListener {
                        activityUtil.go(RateActivity::class.java).put("id", order.id).start()
                    }
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).put("id", order.id).put("type", SingleOrderFragment.FINISHED).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type, order.id)
                                    }

                                    override fun clickButton2(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                    }

                                })
                                .show(fragmentManager, "delete")
                    }
                }
                SingleOrderFragment.CANCELED -> {
                    if (order.state == -1) {
                        viewHolder.tvAccept.text = "Cancellation Reasons"
                        viewHolder.tvCancelReason.text = order.userCancelReason
                    } else {
                        viewHolder.tvAccept.text = "Me-Cancellation Reasons"
                        viewHolder.tvCancelReason.text = order.teacherCancelReason
                    }
                    viewHolder.tvAcceptCount.visibility = View.GONE
                    viewHolder.tvCancelReason.visibility = View.VISIBLE

                    viewHolder.btnCancel.text = "Delete"
                    viewHolder.btnAccept.visibility = View.GONE
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).put("id", order.id).put("type", SingleOrderFragment.CANCELED).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type, order.id)
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
                    viewHolder.cvItem.setOnClickListener {
                        activityUtil.go(OrderDetailActivity::class.java).put("id", order.id).put("type", SingleOrderFragment.DECLINED).start()
                    }
                    viewHolder.btnCancel.setOnClickListener {
                        CustomDialog.Builder()
                                .create()
                                .setTitle(context.getString(R.string.delete_record))
                                .setDialogListene(object : CustomDialog.CustomDialogListener {
                                    override fun clickButton1(customDialog: CustomDialog) {
                                        customDialog.dismiss()
                                        singleOrderItemClickListener.onDialogClick(type, order.id)
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

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        if (holder is SingleOrderAdapter.ViewHolder) {
            holder.disposable?.dispose()
        }

    }

    fun clearAllDisposable() {
        disposableList.map { it.dispose() }
    }

    fun setListener(listener: SingleOrderItemClickListener) {
        singleOrderItemClickListener = listener
    }

    interface SingleOrderItemClickListener {
        fun onDialogClick(type: String, id: Int)
        fun loacte(address: String)
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

        var disposable: CompositeDisposable = CompositeDisposable()

        init {
            ButterKnife.bind(this, view)
        }
    }
}
