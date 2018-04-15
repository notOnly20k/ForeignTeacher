package com.xld.foreignteacher.ui.mine.setting

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.ListActivity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by cz on 4/13/18.
 */
class SettingActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_setting
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Settings")

        tv_change_password.setOnClickListener {
            activityUtil.go(ChangPwdActivity::class.java).start()
        }

        tv_blocked_list.setOnClickListener {
            activityUtil.go(ListActivity::class.java).put("type",ListActivity.BLOCKED_LIST).start()
        }

    }

    override fun initData() {

    }
}