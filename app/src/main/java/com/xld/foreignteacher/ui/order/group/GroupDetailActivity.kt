package com.xld.foreignteacher.ui.order.group

import android.os.Bundle
import android.widget.RadioButton
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.H5Fragment
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.initStatus2(window)
        StatusBarUtil.initBarHeight(this,null,fake_status_bar)
    }

    override fun initView() {
        serviceDetailsFragment = H5Fragment.createInstance("http://weixin.qq.com")
        ruleFragment = H5Fragment.createInstance("http://www.baidu.com")
        iv_back.setOnClickListener {
            finish()
        }
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

    override fun initData() {

    }
}
