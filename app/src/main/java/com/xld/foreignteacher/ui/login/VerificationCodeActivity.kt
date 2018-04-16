package com.xld.foreignteacher.ui.login

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import cn.sinata.xldutils.view.TitleBar
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.NetWork
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.H5Activity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.views.EditEmptyWatcher
import com.xld.foreignteacher.views.PhoneNumEditText

/**
 * Created by cz on 3/29/18.
 */
class VerificationCodeActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {
    override val changeTitleBar: Boolean
        get() = false
    private var register_type: Int = 0
    private var canSendMsg = true
    private var type: String? = null

    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.et_phone)
    lateinit var etPhone: PhoneNumEditText
    @BindView(R.id.et_verification_code)
    lateinit var etVerificationCode: EditText
    @BindView(R.id.btn_get_verification_code)
    lateinit var btnGetVerificationCode: TextView
    @BindView(R.id.btn_login_next)
    lateinit var btnLoginNext: TextView
    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView
    @BindView(R.id.tv_agreement_type)
    lateinit var tvAgreementType: TextView
    @BindView(R.id.ll_agreement)
    lateinit var llAgreement: LinearLayout

    private var timer: CountDownTimer? = object : CountDownTimer(60000, 1000) {
        override fun onTick(l: Long) {
            if (!this@VerificationCodeActivity.isFinishing)
                btnGetVerificationCode.text = (l / 1000).toString() + "s"
        }

        override fun onFinish() {
            if (!this@VerificationCodeActivity.isFinishing) {
                btnGetVerificationCode.text = resources.getString(R.string.login_get_verification_code)
                btnGetVerificationCode.isEnabled = true
                canSendMsg = true
            }
        }
    }

    override val contentViewResId: Int
        get() = R.layout.activity_verification_code

    override fun initView() {
        val intent = intent
        register_type = intent.getIntExtra(FUNC_TYPE, 0)
        titleBar.leftView.setOnClickListener { finish() }
        when (register_type) {
            TYPE_REGISTER -> {
                tvTitle.setText(R.string.log_register_heyllo)
                titleBar.addRightButton(resources.getString(R.string.login_to_login)) { finish() }
            }
            TYPE_THIRD -> {
                tvTitle.text = (R.string.log_bind_phone).toFormattedString(this)
                llAgreement.visibility = View.VISIBLE
                // tvAgreementType.text=(R.string.log_type_bind)
                btnLoginNext.setText(R.string.login_commit)
            }
            TYPE_FORGET -> tvTitle.setText(R.string.find_pwd)
        }

        titleBar.setLeftButton(R.mipmap.icon_back_black, View.OnClickListener { finish() })
        val watcher = EditEmptyWatcher(this)
        etPhone.addTextChangedListener(watcher)
        etVerificationCode.addTextChangedListener(watcher)
    }

    override fun initData() {
        type = null
        when (register_type) {
            TYPE_REGISTER -> type = "5"
            TYPE_FORGET -> type = "6"
            TYPE_CHANGE_PHONE -> type = "4"
        }
    }


    @OnClick(R.id.btn_get_verification_code, R.id.btn_login_next, R.id.tv_service_agreement)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_get_verification_code -> {
                appComponent.netWork.sendMsg(etPhone.text.toString().replace("-", ""), type!!)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe { code ->
                            showToast(getString(R.string.msg_sended))
                            etVerificationCode.setText(code)
                        }

                btnGetVerificationCode.isEnabled = false
                canSendMsg = false
                timer!!.start()
            }
            R.id.btn_login_next -> {
                appComponent.netWork.checkMsg(etPhone.text.toString().replace("-", ""), etVerificationCode.text.toString(), type!!)
                        .doOnLoading { showProgress(it) }
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe { _ -> checkSuccess() }
            }
            R.id.tv_service_agreement -> activityUtil.go(H5Activity::class.java).put("url", appComponent.netWork.getH5Url(NetWork.TYPE_AGREEMENT)).start()
        }
    }

    private fun checkSuccess() {
        when (register_type) {
            TYPE_REGISTER -> {
                activityUtil.go(SetPwdActivity::class.java)
                        .put(SetPwdActivity.SET_PWD_TYPE, SetPwdActivity.TYPE_REGISTER)
                        .put("phone", etPhone.getText().toString().replace("-", ""))
                        .start()
                finish()
            }
            TYPE_THIRD -> {
//                activityUtil.go(InvitationCodeActivity::class.java)
//                        .put(SetPwdActivity.SET_PWD_TYPE, SetPwdActivity.TYPE_REGISTER)
//                        .put("isFirstIn", true)
//                        .put("phone", etPhone.getText().toString().replace("-", ""))
//                        .start()
                finish()
            }
            TYPE_FORGET -> {
                activityUtil.go(SetPwdActivity::class.java)
                        .put(SetPwdActivity.SET_PWD_TYPE, SetPwdActivity.TYPE_FORGET)
                        .put("phone", etPhone.getText().toString().replace("-", ""))
                        .start()
                finish()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        super.onDestroy()
    }

    override fun checkAllEditContent() {
        val verification = etVerificationCode.text.toString().trim { it <= ' ' }
        val phone = etPhone.getText().toString().trim()
        btnLoginNext.isEnabled = TextUtils.getTrimmedLength(phone) == 13 && TextUtils.getTrimmedLength(verification) == 6
        btnGetVerificationCode.isEnabled = canSendMsg && TextUtils.getTrimmedLength(phone) == 13
    }

    companion object {
        val FUNC_TYPE = "FUNC_TYPE"
        val TYPE_REGISTER = 1
        val TYPE_THIRD = 2
        val TYPE_FORGET = 3
        val TYPE_CHANGE_PHONE = 4
    }
}
