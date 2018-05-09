package com.xld.foreignteacher.ui.order.single

import android.os.CountDownTimer
import android.view.Gravity
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_rate.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 4/12/18.
 */
class RateActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_rate
    override val changeTitleBar: Boolean
        get() = false
    var id=-1
    var userId=-1
    var teacherId=-1

    override fun initView() {
        id=intent.getIntExtra("id",-1)
        title_bar.setTitle("Rate")
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })

        val backDra = resources.getDrawable(R.mipmap.icon_woman)
        backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)
        tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
        tv_name.compoundDrawablePadding = backDra.minimumHeight / 2
        tv_accept.setOnClickListener {
            upData()
        }
    }

    fun upData(){
        appComponent.netWork.evlauateUser(id,userId,teacherId,sbv_starbar_listen.getSartRating().toDouble()
                ,sbv_starbar_grammar.getSartRating().toDouble(),
                sbv_starbar_vocabulary.getSartRating().toDouble(),
                sbv_starbar_pronunciation.getSartRating().toDouble(),
                sbv_starbar_fluency.getSartRating().toDouble())
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    showTDialog()
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
                finish()
            }
        }.start()
    }



    override fun initData() {
        val formart = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        appComponent.netWork.getPersonalTrainingOrderDetail(id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    userId=it.userId
                    teacherId=it.teacherId
                    var backDra = if (it.user!!.sex == 1) {
                        resources.getDrawable(R.mipmap.icon_male)
                    } else {
                        resources.getDrawable(R.mipmap.icon_woman)
                    }
                    formart.format(it.addtime)+getTime(it.curriculum!!.startTime,it.curriculum!!.endTime)
                    backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
                    tv_name.compoundDrawablePadding = backDra.minimumHeight / 2

                    tv_title.text = it.curriculum!!.title
                    tv_info.text = it.curriculum!!.className + " " + it.curriculum!!.languages!!.eName
                    tv_person_count.text = "No.Of attendant:${it.numberOfPeople} "
                    iv_head.setImageURI(it.user!!.imgUrl.toString())
                    tv_name.text = it.user!!.nickName
                    tv_age.text = getString(R.string.ages, it.user!!.age)
                }
    }
}