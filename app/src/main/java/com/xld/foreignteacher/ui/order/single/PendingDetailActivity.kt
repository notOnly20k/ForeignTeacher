package com.xld.foreignteacher.ui.order.single

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
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
    var id = -1
    private lateinit var finishAdapter: PendingDetailAdapter
    private lateinit var pendingAdapter: PendingDetailAdapter
    private lateinit var cancelAdapter: PendingDetailAdapter

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Order detail")
        rec_content.layoutManager = LinearLayoutManager(this).apply { LinearLayout.VERTICAL }
        cancelAdapter = PendingDetailAdapter(this, CANCEL, supportFragmentManager)
        cancelAdapter.setListener(object : PendingDetailAdapter.PendingItemClickListener {
            override fun onDialogClick(type: String, id: Int) {
                appComponent.netWork.deletePersonalTrainingOrder(id)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            showSuccessDialog()
                            initData()
                        }
            }

        })
        pendingAdapter = PendingDetailAdapter(this, PENDING, supportFragmentManager)
        pendingAdapter.setListener(object : PendingDetailAdapter.PendingItemClickListener {
            override fun onDialogClick(type: String, id: Int) {
                appComponent.netWork.takeOrder(id)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            showSuccessDialog()
                            initData()
                        }
            }

        })
        finishAdapter = PendingDetailAdapter(this, FINISH, supportFragmentManager)
        finishAdapter.setListener(object : PendingDetailAdapter.PendingItemClickListener {
            override fun onDialogClick(type: String, id: Int) {
                appComponent.netWork.deletePersonalTrainingOrder(id)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            showSuccessDialog()
                            initData()
                        }
            }

        })
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

    fun showSuccessDialog() {
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
    }


    override fun initData() {
        id = intent.getIntExtra("id", -1)
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
                    tv_order_num.text = "Order#:${it.orderNumber}"
                    if (it.numberOfPeople > 1) {
                        tv_client_count.visibility = View.VISIBLE
                        tv_client_count.text = "(${it.numberOfPeople} people)"
                    }
                    iv_head.setImageURI(it.user!!.imgUrl.toString())
                    tv_name.text = it.user!!.nickName
                    tv_age.text = getString(R.string.ages, it.user!!.age)
                }

        appComponent.netWork.getMyPersonalTrainingChildOrder(id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    if (it.CanceledBookingUsers != null) {
                        cancelAdapter.addData(it)
                    }
                    if (it.FinishBookingUsers != null) {
                        finishAdapter.addData(it)
                    }
                    if (it.UnFinishBookingUsers != null) {
                        pendingAdapter.addData(it)
                    }
                }

    }

    companion object {
        const val PENDING = "Pending"
        const val FINISH = "Finish"
        const val CANCEL = "Cancel"
    }

}
