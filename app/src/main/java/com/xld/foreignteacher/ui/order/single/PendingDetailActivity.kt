package com.xld.foreignteacher.ui.order.single

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.order.adapter.PendingDetailAdapter
import kotlinx.android.synthetic.main.activity_pending_order_detail.*

/**
 * Created by cz on 4/15/18.
 */
class PendingDetailActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_pending_order_detail
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var finishAdapter: PendingDetailAdapter
    private lateinit var pendingAdapter: PendingDetailAdapter
    private lateinit var cancelAdapter: PendingDetailAdapter

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Order detail")
        rec_content.layoutManager = LinearLayoutManager(this).apply { LinearLayout.VERTICAL }
        cancelAdapter = PendingDetailAdapter(this)
        pendingAdapter = PendingDetailAdapter(this)
        finishAdapter = PendingDetailAdapter(this)
        rb_pending.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rec_content.adapter = pendingAdapter
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
            } else {
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
            }
        }
        rb_cancel.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rec_content.adapter = cancelAdapter
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
            } else {
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
            }
        }
        rb_finish.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rec_content.adapter = finishAdapter
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
            } else {
                buttonView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL)
            }
        }

        rb_pending.isChecked = true
    }

    override fun initData() {

    }
}
