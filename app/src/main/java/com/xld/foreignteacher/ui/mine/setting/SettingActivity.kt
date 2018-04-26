package com.xld.foreignteacher.ui.mine.setting

import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.formateToTel
import com.xld.foreignteacher.ui.ListActivity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * Created by cz on 4/13/18.
 */
class SettingActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_setting
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Settings")

        tv_tel_num.text = (appComponent.userHandler.getUser().phone?:"").formateToTel()

        tv_change_password.setOnClickListener {
            activityUtil.go(ChangPwdActivity::class.java).start()
        }

        tv_blocked_list.setOnClickListener {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.BLOCKED_LIST).start()
        }
        tv_tel.setOnClickListener {
            activityUtil.go(ChangeMobileActivity::class.java).start()
        }

        tv_logout.setOnClickListener {
            appComponent.userHandler.saveUser(User(sex = 1, id = -1))
            activityUtil.go(LoginActivity::class.java).start()
            closeAll()
        }

    }

    override fun initData() {

    }
}
