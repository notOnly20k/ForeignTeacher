package com.xld.foreignteacher.ui.mine.setting

import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.formateToTel
import com.xld.foreignteacher.ui.ListActivity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.login.LoginActivity
import com.xld.foreignteacher.util.CacheCleanUtil
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

        try {
            tv_data.text=CacheCleanUtil.getTotalCacheSize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tv_data.setOnClickListener {
            CacheCleanUtil.clearAllCache(this)
            try {
                tv_data.text=CacheCleanUtil.getTotalCacheSize(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        tv_tel_num.text = (appComponent.userHandler.getUser().phone ?: "").formateToTel()

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

            EMClient.getInstance().logout(false, object : EMCallBack {
                override fun onSuccess() {
                    showProgress(false)
                    appComponent.userHandler.saveUser(User(sex = 1, id = -1))
                    activityUtil.go(LoginActivity::class.java).start()
                    closeAll()
                }

                override fun onProgress(p0: Int, p1: String?) {
                    showProgress(true)
                }

                override fun onError(p0: Int, p1: String?) {
                    showProgress(false)
                }

            })

        }

    }

    override fun initData() {

    }
}
