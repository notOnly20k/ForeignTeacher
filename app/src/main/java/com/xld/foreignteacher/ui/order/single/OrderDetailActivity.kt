package com.xld.foreignteacher.ui.order.single

import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.order.adapter.OrderDetailAdapter
import com.xld.foreignteacher.ui.report.ReportActivity
import kotlinx.android.synthetic.main.activity_order_detail.*

/**
 * Created by cz on 4/3/18.
 */
class OrderDetailActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_order_detail
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.setTitle("Details")
        title_bar.titlelayout.setBackgroundResource(R.color.black_00)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton("Decline") {
            //todo 传id
            activityUtil.go(ReportActivity::class.java).put("id", 0).put("type",ReportActivity.REPORT).start()
        }
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))

        rec_class_time.adapter = OrderDetailAdapter(this)
        rec_class_time.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }

        tv_accept.setOnClickListener {
            val dialog=TDialog.Builder(supportFragmentManager)
                    .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                    .setScreenWidthAspect(this, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setScreenHeightAspect(this, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                    .setTag("SubscribeDialog")   //设置Tag
                    .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                    .setCancelableOutside(true)     //弹窗在界面外是否可以点击取消
                    .setCancelable(false)    //弹窗是否可以取消
                    .create()   //创建TDialog
                    .show()    //展示
             val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(l: Long) {
                }

                override fun onFinish() {
                    dialog.dismiss()
                    finish()
                }
            }.start()

        }
    }

    override fun initData() {

    }
}
