package com.whynuttalk.foreignteacher.ui.schedule

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.RadioButton
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.TimeUtils
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.NetWork
import com.whynuttalk.foreignteacher.data.PreviewGroupOrder
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.H5Fragment
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_preview_group.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cz on 5/7/18.
 */
class PreviewGroupOrderActivity : BaseTranslateStatusActivity() {
    private var serviceDetailsFragment: BaseFragment? = null
    private var ruleFragment: H5Fragment? = null

    override val contentViewResId: Int
        get() = R.layout.activity_preview_group
    override val changeTitleBar: Boolean
        get() = false
    var type = -1
    var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.initStatus2(window)
        StatusBarUtil.initBarHeight(this, null, fake_status_bar)
    }

    override fun initView() {
        type = intent.getIntExtra("type", -1)
        iv_back.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun initData() {
        val user = appComponent.userHandler.getUser()
        if (type == 11) {
            val id = intent.getIntExtra("id", -1)
            appComponent.netWork.getFightDetail(id, user.lat ?: 0.00, user.lon ?: 0.00)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        val f2 = SimpleDateFormat("yyyy-mm-dd HH:mm", Locale.CHINA)
                        val price = SpannableString("￥${it.price}/person")
                        price.setSpan(RelativeSizeSpan(1.2f), 1, it.price.toString().length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                        tv_price.text = price
                        tv_title.text = it.title
                        val week = TimeUtils.getCurrentTimeMMDD(it.openingTime)
                        val start = TimeUtils.getTimeHM(it.openingTime)
                        val end = TimeUtils.getTimeHM(it.endTime)
                        tv_datetime.text = "timer: $week $start ~ $end"
                        tv_deadline.text = "Sign-up deadline: ${f2.format(it.deadlineRegistration)}"
                        tv_location.text = it.address
                        tv_max.text = "Max.participant:" + it.enrolment
                        tv_min.text = "Min.participant:" + it.classesNumber
                        img_pic.setImageURI(it.backgroundCourse ?: "")
                        iv_head.setImageURI(it.teachersName)
                        tv_name.text = it.teachersName
                        tv_socer.text = it.score.toString() + " points"
                        tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, if (it.sex === 1) resources.getDrawable(R.mipmap.icon_male) else resources.getDrawable(R.mipmap.icon_female), null)
                        url = if (it.courseEtails.contains("http")) {
                            it.courseEtails
                        } else {
                            "http://" + it.courseEtails
                        }
                        initRadio2()
                    }
        } else {

            val data = intent.getSerializableExtra("data") as PreviewGroupOrder
            tv_title.text = data.title
            tv_max.text = "Max.participant:" + data.max
            tv_min.text = "Min.participant:" + data.min
            tv_deadline.text = "Sign-up deadline:" + data.deadline
            tv_location.text = data.address
            tv_datetime.text = data.time
            tv_socer.text = user.point.toString() + " points"
            if (data.price.isNotEmpty()) {
                val price = SpannableString("￥${data.price}/person")
                price.setSpan(RelativeSizeSpan(1.2f), 1, data.price.length + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tv_price.text = price
            }
            tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, if (user.sex === 1) resources.getDrawable(R.mipmap.icon_male) else resources.getDrawable(R.mipmap.icon_female), null)
            img_pic.setImageURI(Uri.fromFile(File(data.backGround)))

            iv_head.setImageURI(user.imgUrl)
            tv_name.text = user.nickName
            initRadio(data)
        }

    }

    private fun initRadio2() {
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

    fun initRadio(data: PreviewGroupOrder) {

        serviceDetailsFragment = ServiceDetailFragment.createInstance(data.introduction, data.pics as ArrayList<String>)
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
}
