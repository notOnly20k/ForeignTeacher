package com.xld.foreignteacher.ui.report

import android.view.Gravity
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.views.InformItemView
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_report
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        val type = intent.getStringExtra("type")
        title_bar.titlelayout.setBackgroundResource(R.color.black_00)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        when (type) {
            REPORT -> {
                title_bar.setTitle(REPORT)
                tv_ads_promotion.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_false_info.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_fraud.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_harassing.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_lewd_or_harassing_content.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_violation_user_name.setOnClickListener { infoViewClicked(it as InformItemView) }
                btn_commit.setOnClickListener {
                    val reason = et_reason.text.toString()
                    showTDialog()
                }
            }
            CANCEL_REQUEST -> {
                title_bar.setTitle(CANCEL_REQUEST)
                tv_title.text = "Reason for cancellation"
                tv_other_reason.text = "Details"

                tv_ads_promotion.text = "Time clash"
                tv_false_info.text = "Sick"
                tv_harassing.text = "Emergency condition"
                tv_lewd_or_harassing_content.text = "Wrong time setting"
                tv_violation_user_name.text = "Other"

                tv_ads_promotion.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_false_info.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_fraud.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_harassing.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_lewd_or_harassing_content.setOnClickListener { infoViewClicked(it as InformItemView) }
                tv_violation_user_name.setOnClickListener { infoViewClicked(it as InformItemView) }
            }
        }
    }


    fun showTDialog() {
        TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                .setScreenWidthAspect(this, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("SubscribeDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelableOutside(true)     //弹窗在界面外是否可以点击取消
                .setCancelable(true)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示
    }

    private fun infoViewClicked(view: InformItemView) {
        view.checked = !view.checked
        changeBtnStatus()
    }

    private fun changeBtnStatus() {
        btn_commit.isEnabled = tv_ads_promotion.checked || tv_false_info.checked || tv_fraud.checked ||
                tv_harassing.checked || tv_lewd_or_harassing_content.checked || tv_violation_user_name.checked
    }

    override fun initData() {

    }

    companion object {
        const val REPORT = "Report"
        const val CANCEL_REQUEST = "Cancel request"
    }

}