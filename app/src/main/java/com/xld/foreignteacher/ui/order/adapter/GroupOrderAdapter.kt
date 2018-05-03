package com.xld.foreignteacher.ui.order.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import cn.sinata.xldutils.adapter.LoadMoreAdapter
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.TimeUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.GroupOrder
import com.xld.foreignteacher.ui.order.group.GroupOrdersFragment
import com.xld.foreignteacher.views.RoundBackgroundColorSpan
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * Created by cz on 4/8/18.
 */
class GroupOrderAdapter(private val context: Context, private val fragmentManager: FragmentManager, private val type: String) : LoadMoreAdapter() {

    private val activityUtil: ActivityUtil = ActivityUtil.create(context)
    private val logger = LoggerFactory.getLogger("GroupOrderAdapter")
    private val data = mutableListOf<GroupOrder>()
    private var callback: OnItemClickCallback? = null

    private val disposableList: MutableList<Disposable> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            holder as GroupOrderViewHolder
            if (holder.disposable.size() != 0) {
                holder.disposable.dispose()
            }
            val groupOrder = data[position]
            val tag = groupOrder.eName ?: ""
            val title = SpannableString(tag + "   " + groupOrder.title)
            title.setSpan(RoundBackgroundColorSpan(context, Color.parseColor("#FCFFE0"), context.resources.getColor(R.color.yellow_ffcc00), 13), 0, tag.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            holder.tvPinpinTile.text = title
            holder.ivPinpinImg.setImageURI(groupOrder.backgroundCourse ?: "")
            holder.tvLocation.text = groupOrder.address ?: ""
            val price = SpannableString("￥${groupOrder.price}/person")
            price.setSpan(RelativeSizeSpan(1.5f), 1, groupOrder.price.toString().length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            holder.tvPinpinPrice.text = price
            val week = TimeUtils.getCurrentTimeMMDD(groupOrder.openingTime)
            val start = TimeUtils.getTimeHM(groupOrder.openingTime)
            val end = TimeUtils.getTimeHM(groupOrder.endTime)
            holder.tvDatetime.text = "$week $start ~ $end"
            when (type) {
                GroupOrdersFragment.FOR_SALE -> {
                    val backDra = context.resources.getDrawable(R.mipmap.icon_like)
                    backDra.setBounds(0, 0, backDra.minimumHeight, backDra.minimumHeight)
                    holder.tvDelete.visibility = View.GONE
                    holder.tvPinpinPersonCount.setCompoundDrawables(backDra, null, null, null)
                    holder.tvPinpinPersonCount.text = groupOrder.number.toString()
                    holder.tvTimer.visibility = View.VISIBLE
                    Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                            .doOnSubscribe {
                                holder.disposable.add(it)
                                disposableList.add(it)
                            }
                            .map {
                                if (groupOrder.deadlineRegistration - System.currentTimeMillis() > 0) {
                                    DateUtils.formatElapsedTime((groupOrder.deadlineRegistration - System.currentTimeMillis()) / 1000L)
                                } else {
                                    "00:00:00"
                                }
                            }
                            .subscribe {
                                holder.tvTimer.text = "For sale after: $it"
                                if (it == "00:00:00") {
                                    holder.disposable.dispose()
                                }
                            }
                }
                GroupOrdersFragment.OPEN -> {
                    holder.tvDelete.visibility = View.GONE
                    holder.tvPinpinPersonCount.text = "${groupOrder.number}/${groupOrder.enrolment}"
                }
                GroupOrdersFragment.PENDING -> {
                    holder.tvDelete.visibility = View.GONE
                }
                GroupOrdersFragment.FINISHED -> {
                    holder.tvDelete.setOnClickListener {
                        callback?.onDeletItem(groupOrder.id)
                    }
                }
                GroupOrdersFragment.CANCELED -> {
                    holder.tvDelete.setOnClickListener {
                        callback?.onDeletItem(groupOrder.id)
                    }
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

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        if (holder is GroupOrderViewHolder) {
            holder.disposable?.dispose()
        }

    }

    fun clearAllDisposable(){
        disposableList.map { it.dispose() }
    }


    fun setDataList(list: List<GroupOrder>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
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

        var disposable: CompositeDisposable = CompositeDisposable()

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                if (callback != null)
                    callback!!.onItemClick(data[adapterPosition].id)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClick(id: Int)
        fun onDeletItem(id: Int)
    }


    fun setCallback(callback: OnItemClickCallback) {
        this.callback = callback
    }


}
