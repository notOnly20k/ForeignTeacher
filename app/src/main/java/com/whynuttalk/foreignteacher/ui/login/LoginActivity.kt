package com.whynuttalk.foreignteacher.ui.login


import android.Manifest
import android.annotation.SuppressLint
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnCheckedChanged
import butterknife.OnClick
import cn.sinata.xldutils.view.TitleBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.tbruyelle.rxpermissions2.RxPermissions
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.User
import com.whynuttalk.foreignteacher.ext.*
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.main.MainActivity
import com.whynuttalk.foreignteacher.views.PhoneNumEditText
import io.reactivex.android.schedulers.AndroidSchedulers


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
        val phone = etPhone.text.toString().trim()
        btnLoginCommit.isEnabled = TextUtils.getTrimmedLength(phone) == 13 && TextUtils.getTrimmedLength(pwd) >= 6
    }

    override fun initData() {
        //activityUtil.go(EditTeacherInfoActivity::class.java).start()
        val id = appComponent.userHandler.getUser().id

        if (EMClient.getInstance().isLoggedInBefore&&id!=-1) {
            activityUtil.go(MainActivity::class.java).start()
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        requestPermissions()
        super.onResume()
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
                        Manifest.permission.CAMERA
                )
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
                logger.e { etPwd.text.toString().toMD5() }
                appComponent.netWork.login(etPhone.text.toString().formateToNum(), etPwd.text.toString().toMD5())
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe { user ->
                           // SPUtils.save("id", user.id)
                            appComponent.userHandler.saveUser(user)
                            login(user)

                        }
            }
            R.id.tv_login_forget_pwd -> {
                //activityUtil.go(EditTeacherInfoActivity::class.java).put("type",EditTeacherInfoActivity.SAVE).start()
                activityUtil.go(VerificationCodeActivity::class.java)
                        .put(VerificationCodeActivity.FUNC_TYPE, VerificationCodeActivity.TYPE_FORGET)
                        .start()
            }
        }
    }

    @OnCheckedChanged(R.id.ck_password)
    fun OnCheckedChanged(boolean: Boolean) = if (boolean) {
        etPwd.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
    } else {
        etPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    }

    //环信登录的方法
    fun login(user: User) {
        showProgress(true)
        logger.e { "login" }
        EMClient.getInstance().login("waijiao_teacher_" + user.id, "123456", object : EMCallBack {
            override fun onSuccess() {
                showProgress(false)
                activityUtil.go(MainActivity::class.java).start()

                finish()
            }

            override fun onError(i: Int, s: String) {
                showProgress(false)
               // showToast("环信登录失败:code=" + i + "reason" + s)
                Log.e("Login", "环信登录失败:code=" + i + "reason" + s)
                if (i == 204)
                //如果账号不存在，重新注册
                    register(user)
            }

            override fun onProgress(i: Int, s: String) {

            }
        })
    }

    //环信注册的方法 异步
    fun register(user: User) {
        Thread(Runnable {
            try {
                EMClient.getInstance().createAccount("waijiao_teacher_" + user.id, "123456")
                Log.e("Register", "环信注册成功")
                login(user)
            } catch (e: HyphenateException) {
                e.printStackTrace()
                Log.e("Register", "环信注册失败")
            }
        }).start()
    }
}
