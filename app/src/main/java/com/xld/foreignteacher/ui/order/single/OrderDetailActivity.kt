package com.xld.foreignteacher.ui.order.single

import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.text.format.DateUtils
import android.view.Gravity
import android.view.View
import cn.sinata.xldutils.utils.TimeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.data.BookInfo
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.order.adapter.OrderDetailAdapter
import com.xld.foreignteacher.ui.report.ReportActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by cz on 4/3/18.
 */
class OrderDetailActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_order_detail
    override val changeTitleBar: Boolean
        get() = false

    lateinit var adapter: OrderDetailAdapter

    var id = -1
    var type = ""

    override fun initView() {
        type = intent.getStringExtra("type")
        title_bar.setTitle("Order detail")
        id = intent.getIntExtra("id", -1)
        when (type) {
            SingleOrderFragment.NEW_ORDERS -> {
                title_bar.addRightButton("Decline") {
                    activityUtil.go(ReportActivity::class.java).put("id", 0).put("type", ReportActivity.CANCEL_MAIN_REQUEST).start()
                }
                tv_lab.visibility = View.GONE
                title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
            }
            SingleOrderFragment.CANCELED -> {
                tv_accept_time.text = "Me-Cancellation Reasons"
                tv_accept_count.visibility = View.GONE
                tv_lab.text = ""
                tv_accept.text = "Delete"
                tv_accept.setBackgroundResource(R.drawable.bg_grey_button)
            }
            SingleOrderFragment.DECLINED -> {
                tv_accept_time.text = "Refusal cause"
                tv_accept_count.visibility = View.GONE
                tv_lab.text = ""
                tv_accept.text = "Delete"
                tv_accept.setBackgroundResource(R.drawable.bg_grey_button)
            }
            SingleOrderFragment.FINISHED -> {
                tv_accept.text = "Rate"
                tv_accept_time.visibility = View.GONE
                tv_accept_count.visibility = View.GONE
                tv_lab.visibility = View.GONE
            }
        }
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })



        adapter = OrderDetailAdapter(this)
        rec_class_time.adapter = adapter
        rec_class_time.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }

        tv_accept.setOnClickListener {
            when (type) {
                SingleOrderFragment.DECLINED, SingleOrderFragment.CANCELED -> {
                    CustomDialog.Builder()
                            .create()
                            .setTitle(getString(R.string.delete_record))
                            .setDialogListene(object : CustomDialog.CustomDialogListener {
                                override fun clickButton1(customDialog: CustomDialog) {
                                    appComponent.netWork.deletePersonalTrainingOrder(id)
                                            .doOnSubscribe { mCompositeDisposable.add(it) }
                                            .doOnLoading { isShowDialog(it) }
                                            .subscribe {
                                                customDialog.dismiss()
                                                showSuccessDialog()
                                            }

                                }

                                override fun clickButton2(customDialog: CustomDialog) {
                                    customDialog.dismiss()
                                }

                            })
                            .show(supportFragmentManager, "delete")
                }
                SingleOrderFragment.FINISHED -> {
                    activityUtil.go(RateActivity::class.java).put("id", id).start()
                }
                SingleOrderFragment.NEW_ORDERS -> {
                    appComponent.netWork.takeOrder(id)
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .doOnLoading { isShowDialog(it) }
                            .subscribe {
                                showSuccessDialog()
                            }
                }
            }
        }
    }

    fun showSuccessDialog() {
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
        appComponent.netWork.getPersonalTrainingOrderDetail(id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    var backDra = if (it.user!!.sex == 1) {
                        resources.getDrawable(R.mipmap.icon_male)
                    } else {
                        resources.getDrawable(R.mipmap.icon_woman)
                    }
                    backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
                    tv_name.compoundDrawablePadding = backDra.minimumHeight / 2

                    tv_title.text = it.curriculum!!.title
                    tv_location.text = it.address
                    tv_price.text = getString(R.string.price_class, it.curriculum!!.price.toInt())
                    tv_info.text = it.curriculum!!.className + " " + it.curriculum!!.languages!!.eName
                    tv_order_num.text = "Order#${it.orderNumber}"

                    tv_client_count.text = "${it.numberOfPeople} people"
                    iv_head.setImageURI(it.user!!.imgUrl.toString())
                    tv_name.text = it.user!!.nickName
                    tv_age.text = getString(R.string.ages, it.user!!.age)
                    if (it.bookingInfo != null && it.bookingInfo!!.isNotEmpty()) {
                        val typeToken = object : TypeToken<List<BookInfo>>() {

                        }.type
                        val list = Gson().fromJson<List<BookInfo>>(it.bookingInfo, typeToken)
                        adapter.setData(list, it.bookingAutoWeeks)
                    }

                    when (type) {
                        SingleOrderFragment.NEW_ORDERS -> {
                            if (it.curriculum != null) {
                                Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                        .doOnSubscribe { mCompositeDisposable.add(it) }
                                        .map { ignore ->
                                            val formart = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                            val calend = Calendar.getInstance()
                                            calend.time = Date(it.curriculum!!.craeteTime)
                                            calend.set(Calendar.HOUR, calend.get(Calendar.HOUR) + 1)
                                            val time = TimeUtils.parseTimeMillisecond(formart.format(calend.time))
                                            if (time - System.currentTimeMillis() > 0) {
                                                DateUtils.formatElapsedTime((time - System.currentTimeMillis()) / 1000L)
                                            } else {
                                                "00:00:00"
                                            }
                                        }
                                        .subscribe {
                                            tv_accept_count.text = it
                                        }
                            }
                        }
                        SingleOrderFragment.CANCELED -> {
                            tv_lab.text = it.teacherCancelReason
                        }
                        SingleOrderFragment.DECLINED -> {
                            tv_lab.text = it.userCancelReason
                        }
                        SingleOrderFragment.FINISHED -> {


                        }
                    }
                }
    }
}
