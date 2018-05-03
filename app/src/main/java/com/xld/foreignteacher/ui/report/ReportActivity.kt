package com.xld.foreignteacher.ui.report

import android.app.Activity
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.TextView
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.views.InformItemView
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_report
    override val changeTitleBar: Boolean
        get() = false
    private var lastView: InformItemView? = null
    private var reason = ""
    private var id = -1
    private var detail = ""
    private lateinit var type: String

    override fun initView() {
        type = intent.getStringExtra("type")
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
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
                id = intent.getIntExtra("id", -1)
                detail = et_reason.text.toString()
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
                btn_commit.setOnClickListener {
                    showDeclinDialog()
                }
            }
        }
    }

    fun showDeclinDialog() {
        TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_cancel_confirm)    //设置弹窗展示的xml布局
                .setOnBindViewListener { viewHolder ->
                    viewHolder.getView<TextView>(R.id.tv_no)
                    viewHolder.getView<TextView>(R.id.tv_yes)
                    viewHolder.getView<TextView>(R.id.tv_info).text = R.string.cancel_confirm_info.toFormattedString(this)
                }
                .addOnClickListener(R.id.tv_no, R.id.tv_yes)
                .setOnViewClickListener { viewHolder, view, tDialog ->
                    when (view.id) {
                        R.id.tv_no -> {
                            tDialog.dismiss()
                        }
                        R.id.tv_yes -> {
                            tDialog.dismiss()

                            appComponent.netWork.cancelFigh(id, reason, detail)
                                    .doOnSubscribe { mCompositeDisposable.add(it) }
                                    .doOnLoading { showProgress(it) }
                                    .subscribe { showTDialog() }

                        }
                    }
                }
                .setScreenWidthAspect(this, 1f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 1f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setTag("DeclinDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelableOutside(true)     //弹窗在界面外是否可以点击取消
                .setCancelable(true)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示
    }

    fun showTDialog() {
        val dialog = TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                .setScreenWidthAspect(this, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("SubscribeDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelable(false)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示

        val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {
            }

            override fun onFinish() {
                dialog?.dismiss()
                if (type == CANCEL_REQUEST) {
                    setResult(Activity.RESULT_OK)
                }
                finish()
            }
        }.start()
    }

    private fun infoViewClicked(view: InformItemView) {
        view.checked = !view.checked
        changeBtnStatus()
        if (view.checked) {
            reason = view.text
            if (lastView != null && lastView != view) {
                lastView!!.checked = false
            }

            lastView = view
        } else {
            reason = ""
        }
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
