package com.xld.foreignteacher.ui.mine.setting

import android.os.CountDownTimer
import android.text.TextUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.views.EditEmptyWatcher
import kotlinx.android.synthetic.main.activity_change_pwd.*

/**
 * Created by cz on 4/13/18.
 */
class ChangPwdActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {


    override val contentViewResId: Int
        get() = R.layout.activity_change_pwd
    override val changeTitleBar: Boolean
        get() = false
    private var canSendMsg = true
    private var timer: CountDownTimer? = object : CountDownTimer(60000, 1000) {
        override fun onTick(l: Long) {
            if (!this@ChangPwdActivity.isFinishing)
                btn_get_verification_code.text = (l / 1000).toString() + "s"
        }

        override fun onFinish() {
            if (!this@ChangPwdActivity.isFinishing) {
                btn_get_verification_code.text = resources.getString(R.string.login_get_verification_code)
                btn_get_verification_code.isEnabled = true
                canSendMsg = true
            }
        }
    }


    override fun checkAllEditContent() {
        val verification = et_verification_code.text.toString().trim { it <= ' ' }
        val phone = et_phone.getText().toString().trim()
        btn_next.isEnabled = TextUtils.getTrimmedLength(phone) == 13 && TextUtils.getTrimmedLength(verification) == 6
        btn_get_verification_code.isEnabled = canSendMsg && TextUtils.getTrimmedLength(phone) == 13
    }


    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Change Password")
        val watcher = EditEmptyWatcher(this)
        et_phone.addTextChangedListener(watcher)
        et_verification_code.addTextChangedListener(watcher)

        btn_get_verification_code.setOnClickListener {
            appComponent.netWork.sendMsg(et_phone.text.toString().replace("-", ""), "6")
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { code ->
                        showToast(getString(R.string.msg_sended))
                        et_verification_code.setText(code)
                    }

            btn_get_verification_code.isEnabled = false
            canSendMsg = false
            timer!!.start()
        }

        btn_next.setOnClickListener {
            activityUtil.go(ResetPwdActivity::class.java).put("phone", et_phone.text.toString().replace("-", "")).start()
        }
    }

    override fun initData() {
    }

    override fun onDestroy() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        super.onDestroy()
    }
}
