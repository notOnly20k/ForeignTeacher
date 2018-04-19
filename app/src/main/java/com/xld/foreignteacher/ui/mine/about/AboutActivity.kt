package com.xld.foreignteacher.ui.mine.about

import cn.sinata.xldutils.utils.Utils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.formateToNum
import com.xld.foreignteacher.ui.H5Activity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by cz on 4/13/18.
 */
class AboutActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_about
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("About us")

        tv_feedback.setOnClickListener {
            activityUtil.go(FeedBackActivity::class.java).start()
        }

        tv_about_app.setOnClickListener {
            activityUtil.go(H5Activity::class.java).put("url",appComponent.netWork.getH5Url(NetWork.TYPE_ABOUT_US)).start()
        }

        tv_agreement.setOnClickListener {
            activityUtil.go(H5Activity::class.java).put("url",appComponent.netWork.getH5Url(NetWork.TYPE_AGREEMENT)).start()
        }

        tv_tel.setOnClickListener {
            val tel = tv_tel.text.toString().formateToNum()
            Utils.callPhone(this, tel)
        }
    }

    override fun initData() {

    }
}
