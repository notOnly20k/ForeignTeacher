package com.whynuttalk.foreignteacher.ui.report

import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import com.timmy.tdialog.TDialog
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.data.DeclineReason
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.report.adapter.DeclinedAdapter
import kotlinx.android.synthetic.main.activity_declined.*

/**
 * Created by cz on 4/8/18.
 */
class DeclinedActivity : BaseTranslateStatusActivity() {
    private lateinit var adapter: DeclinedAdapter
    private var list = mutableListOf<DeclineReason>()
    override val contentViewResId: Int
        get() = R.layout.activity_declined
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        val id = intent.getIntExtra("id", -1)
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Declined")
        btn_commit.isEnabled = false
        btn_commit.setOnClickListener {
            val reason = list.find { it.isSelect }

            appComponent.netWork.refuseOrder(id, reason!!.reason)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        showTDialog()
                    }


        }
        adapter = DeclinedAdapter(this, emptyList())
        rec_reason.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
        rec_reason.adapter = adapter
        adapter.setListener(object : DeclinedAdapter.OnItemSelectedListener {
            override fun onItemSelected(declineReason: DeclineReason, position: Int) {
                list.map { it.isSelect = false }
                list[position].isSelect = true
                adapter.upDateList(list)
                btn_commit.isEnabled = true
            }

        })
    }

    fun showTDialog() {
        val dialog = TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                .setScreenWidthAspect(this, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(this, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("SubscribeDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelable(true)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示

        val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(l: Long) {
            }

            override fun onFinish() {
                dialog?.dismiss()
                finish()
            }
        }.start()
    }

    override fun initData() {
        list.add(DeclineReason("Sorry,this location is too far for me,please choose another then order again", false))
        list.add(DeclineReason("Sorry,too short notice,please choose another time", false))
        list.add(DeclineReason("Sorry,my offer doesn't match your request", false))
        list.add(DeclineReason("Sorry,not enough information for me to understand your request", false))
        list.add(DeclineReason("Sorry,I need to attend an emergency", false))
        adapter.upDateList(list)
    }
}
