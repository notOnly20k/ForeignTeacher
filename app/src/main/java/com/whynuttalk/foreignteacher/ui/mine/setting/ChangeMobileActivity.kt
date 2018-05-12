package com.whynuttalk.foreignteacher.ui.mine.setting

import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.e
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.views.EditEmptyWatcher
import kotlinx.android.synthetic.main.activity_change_phone.*


/**
 * Created by cz on 4/25/18.
 */
class ChangeMobileActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {
    override val contentViewResId: Int
        get() = R.layout.activity_change_phone
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Change mobile number")
        val watcher = EditEmptyWatcher(this)
        et_pwd.addTextChangedListener(watcher)
        btn_next.setOnClickListener {
            // activityUtil.go(ResetMobileActivity::class.java).start()
            logger.e { appComponent.userHandler.getUser() }
            if (commitCheck()) {
                appComponent.netWork.checkPassWord(appComponent.userHandler.getUser().id, et_pwd.text.toString())
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            activityUtil.go(ResetMobileActivity::class.java).start()
                        }
            }
        }

    }

    override fun checkAllEditContent() {
        btn_next.isEnabled = et_pwd.text.isNotEmpty()
    }

    override fun commitCheck(): Boolean {
        if (et_pwd.text.isEmpty()) {
            return false
        }
        return super.commitCheck()
    }

    override fun initData() {
    }
}
