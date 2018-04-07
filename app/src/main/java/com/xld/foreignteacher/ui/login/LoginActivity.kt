package com.xld.foreignteacher.ui.login


import android.Manifest
import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnCheckedChanged
import butterknife.OnClick
import cn.sinata.xldutils.utils.SPUtils
import cn.sinata.xldutils.view.TitleBar
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.toMD5
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.main.MainActivity
import com.xld.foreignteacher.ui.userinfo.EditTeacherInfoActivity
import com.xld.foreignteacher.views.PhoneNumEditText


/**
 * Created by cz on 3/27/18.
 */
class LoginActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = true

    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.et_phone)
    lateinit var etPhone: PhoneNumEditText
    @BindView(R.id.et_pwd)
    lateinit var etPwd: EditText
    @BindView(R.id.btn_login_commit)
    lateinit var btnLoginCommit: TextView
    @BindView(R.id.ck_password)
    lateinit var ckpassword: CheckBox


    override val contentViewResId: Int
        get() = R.layout.activity_login


    override fun initView() {
        titleBar.addRightButton(resources.getString(R.string.login_to_register)) {
            activityUtil.go(VerificationCodeActivity::class.java)
                    .put(VerificationCodeActivity.FUNC_TYPE, VerificationCodeActivity.TYPE_REGISTER)
                    .start()
        }
        titleBar.setLeftButton(R.mipmap.icon_close, {
            activityUtil.go(MainActivity::class.java).start()
            finish()
        })
        val emptyWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                checkAllEditContext()
            }
        }
        etPhone.addTextChangedListener(emptyWatcher)
        etPwd.addTextChangedListener(emptyWatcher)
    }

    private fun checkAllEditContext() {
        val pwd = etPwd.text.toString().trim { it <= ' ' }
        val phone = etPhone.getText().toString().trim()
        btnLoginCommit.isEnabled = TextUtils.getTrimmedLength(phone) == 13 && TextUtils.getTrimmedLength(pwd) >= 6
    }

    override fun initData() {
        //activityUtil.go(EditTeacherInfoActivity::class.java).start()
        val id = SPUtils.getInt("id")
        if (id != -1) {
            activityUtil.go(MainActivity::class.java).start()
            finish()
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        super.onResume()
        // requestPermissions()
    }

    fun requestPermissions() {
        val rxPermission = RxPermissions(this@LoginActivity)
        rxPermission
                .requestEach(Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.USE_SIP,
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BROADCAST_STICKY,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.CAMERA)
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                            // 用户已经同意该权限

                        }
                        permission.shouldShowRequestPermissionRationale -> // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            showToast("请同意所有权限，app才能正常运行")
                        else -> {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            showToast("请同意所有权限，app才能正常运行")
                        }
                    }
                }


    }


    @OnClick(R.id.btn_login_commit, R.id.tv_login_forget_pwd)
    fun onViewClicked(view: View) {
        when (view.id) {

            R.id.btn_login_commit -> {
                appComponent.netWork.login(etPhone.text.toString().replace("-", ""), etPwd.text.toString().toMD5())
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .subscribe { user ->
                            activityUtil.go(MainActivity::class.java).start()
                            SPUtils.save("id", user.id)
                            SPUtils.save("phone", user.phone)
                            finish()
                        }
            }
            R.id.tv_login_forget_pwd -> {
                activityUtil.go(EditTeacherInfoActivity::class.java).start()
//                activityUtil.go(VerificationCodeActivity::class.java)
//                        .put(VerificationCodeActivity.FUNC_TYPE, VerificationCodeActivity.TYPE_FORGET)
//                        .start()
            }
        }
    }

    @OnCheckedChanged(R.id.ck_password)
    fun OnCheckedChanged(boolean: Boolean) = if (boolean) {
        etPwd.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
    } else {
        etPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }
}
