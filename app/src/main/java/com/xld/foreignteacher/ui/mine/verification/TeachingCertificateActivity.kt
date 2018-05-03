package com.xld.foreignteacher.ui.mine.verification

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_teaching_certificate.*

/**
 * Created by cz on 4/10/18.
 */
class TeachingCertificateActivity: BaseTranslateStatusActivity() {

    override val contentViewResId: Int
        get() = R.layout.activity_teaching_certificate
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.setTitle((R.string.teaching_certificate).toFormattedString(this))
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        tv_add.setOnClickListener {
            activityUtil.go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.CERTIFICATE).start()
        }
    }

    override fun initData() {
        appComponent.netWork
    }
}