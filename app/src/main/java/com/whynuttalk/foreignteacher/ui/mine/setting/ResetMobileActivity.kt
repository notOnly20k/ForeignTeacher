package com.whynuttalk.foreignteacher.ui.mine.setting

import android.os.CountDownTimer
import android.text.TextUtils
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.formateToNum
import com.whynuttalk.foreignteacher.ext.isPhoneNumberValid
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.views.EditEmptyWatcher
import kotlinx.android.synthetic.main.activity_reset_phone.*

/**
 * Created by cz on 4/25/18.
 */
class ResetMobileActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {
    override val contentViewResId: Int
        get() = R.layout.activity_reset_phone
    override val changeTitleBar: Boolean
        get() = false
    private var canSendMsg = true
    private var timer: CountDownTimer? = object : CountDownTimer(60000, 1000) {
        override fun onTick(l: Long) {
            if (!this@ResetMobileActivity.isFinishing)
                btn_get_verification_code.text = (l / 1000).toString() + "s"
        }

        override fun onFinish() {
            if (!this@ResetMobileActivity.isFinishing) {
                btn_get_verification_code.text = resources.getString(R.string.login_get_verification_code)
                btn_get_verification_code.isEnabled = true
                canSendMsg = true
            }
        }
    }

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Change mobile number")
        val watcher = EditEmptyWatcher(this)
        et_phone.addTextChangedListener(watcher)
        et_verification_code.addTextChangedListener(watcher)
        btn_get_verification_code.setOnClickListener {
            appComponent.netWork.sendMsg(et_phone.text.toString().formateToNum(), "4")
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { code ->
                        showToast(getString(R.string.msg_sended))
                        et_verification_code.setText(code)
                    }

            et_verification_code.isEnabled = false
            canSendMsg = false
            timer!!.start()
        }

        btn_login_commit.setOnClickListener {
            if (commitCheck())
            appComponent.netWork.checkMsg(et_phone.text.toString(), et_verification_code.text.toString(), "4")
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        activityUtil.go(SettingActivity::class.java).start()
                        finish()
                    }
        }
    }

    override fun checkAllEditContent() {
        val verification = et_verification_code.text.toString().trim { it <= ' ' }
        val phone = et_phone.text.toString().trim()
        btn_login_commit.isEnabled = TextUtils.getTrimmedLength(phone) == 13 && TextUtils.getTrimmedLength(verification) == 6
        btn_get_verification_code.isEnabled = canSendMsg && TextUtils.getTrimmedLength(phone) == 13
    }

    override fun initData() {
    }

    override fun commitCheck(): Boolean {
        if (!et_phone.text.toString().formateToNum().isPhoneNumberValid()){
            showToast("The phone is not in the correct format")
            return false
        }
        return super.commitCheck()
    }

    override fun onDestroy() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        super.onDestroy()
    }
}
