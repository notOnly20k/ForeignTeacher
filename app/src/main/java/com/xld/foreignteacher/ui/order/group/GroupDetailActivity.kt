package com.xld.foreignteacher.ui.order.group

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.RadioButton
import cn.sinata.xldutils.utils.TimeUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.H5Fragment
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.report.ReportActivity
import com.xld.foreignteacher.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_group_detail.*


/**
 * Created by cz on 4/8/18.
 */
class GroupDetailActivity : BaseTranslateStatusActivity() {

    private var serviceDetailsFragment: H5Fragment? = null
    private var ruleFragment: H5Fragment? = null

    override val contentViewResId: Int
        get() = R.layout.activity_group_detail
    override val changeTitleBar: Boolean
        get() = false
    private var id: Int = -1
    private lateinit var type: String
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.initStatus2(window)
        StatusBarUtil.initBarHeight(this, null, fake_status_bar)
    }

    override fun initView() {
        id = intent.getIntExtra("id", -1)
        type = intent.getStringExtra("type")
        initMyData()


        if (type == GroupOrdersFragment.PENDING || type == GroupOrdersFragment.FOR_SALE || type == GroupOrdersFragment.OPEN) {
            tv_delete.visibility = View.VISIBLE
        }

        iv_back.setOnClickListener {
            finish()
        }
        tv_delete.setOnClickListener {
            activityUtil.go(ReportActivity::class.java).put("type", ReportActivity.CANCEL_REQUEST)
                    .put("id", id).startForResult(1)

        }

    }

    fun initRadio() {
        logger.e { url }
        serviceDetailsFragment = H5Fragment.createInstance(url)
        ruleFragment = H5Fragment.createInstance(appComponent.netWork.getH5Url(NetWork.TYPE_PINPIN_RULES))

        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_replace, serviceDetailsFragment).commit()

        (rb_class_rules as RadioButton).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val transaction = supportFragmentManager.beginTransaction()
                if (!ruleFragment!!.isAdded) {
                    transaction.add(R.id.fl_replace, ruleFragment)
                }
                if (!ruleFragment!!.isVisible) {
                    transaction.hide(serviceDetailsFragment)
                            .show(ruleFragment).commit()
                }
            }
        }

        (rb_service_details as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (serviceDetailsFragment!!.isVisible) {
                    return@setOnCheckedChangeListener
                }
                supportFragmentManager.beginTransaction()
                        .hide(ruleFragment)
                        .show(serviceDetailsFragment).commit()

            }
        }

        (rb_service_details as RadioButton).isChecked = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            finish()
        }
    }

    private fun initMyData() {
        appComponent.netWork.getFightDetail(id, 0.00, 0.00)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { showProgress(it) }
                .subscribe {
                    val price = SpannableString("￥${it.price}/person")
                    price.setSpan(RelativeSizeSpan(1.2f), 1, it.price.toString().length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    tv_price.text = price
                    val personCount = SpannableString("${it.number1}/${it.classesNumber}")
                    personCount.setSpan(RelativeSizeSpan(1.2f), 0, it.number1.toString().length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    tv_person.text = personCount
                    tv_title.text = it.title
                    val week = TimeUtils.getCurrentTimeMMDD(it.openingTime)
                    val start = TimeUtils.getTimeHM(it.openingTime)
                    val end = TimeUtils.getTimeHM(it.endTime)
                    tv_datetime.text = "timer: $week $start ~ $end"
                    tv_location.text = it.address
                    img_pic.setImageURI(it.backgroundCourse ?: "")
                    var tips = ""
                    if (it.orderInfo != null && it.orderInfo!!.isNotEmpty()) {
                        it.orderInfo!!.indices.map { i ->
                            if (i >= 2) {
                                tips = "${it.orderInfo!![0].nickName},${it.orderInfo!![1].nickName} and ${it.orderInfo!!.size} others have signed up"
                            }
                        }
                    }

                    url = if (it.courseEtails.contains("http")) {
                        it.courseEtails
                    } else {
                        "http://" + it.courseEtails
                    }
                    initRadio()
                    tv_tips.text = tips
                }
    }

    override fun initData() {

    }
}
