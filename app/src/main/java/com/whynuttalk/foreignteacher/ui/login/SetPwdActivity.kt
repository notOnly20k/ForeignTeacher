package com.whynuttalk.foreignteacher.ui.login

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.view.TitleBar
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.NetWork
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.toMD5
import com.whynuttalk.foreignteacher.ui.H5Activity
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.userinfo.EditTeacherInfoActivity
import com.whynuttalk.foreignteacher.views.EditEmptyWatcher

/**
 * Created by cz on 3/29/18.
 */
class SetPwdActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {
    override val changeTitleBar: Boolean
        get() = false
    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView
    @BindView(R.id.et_pwd)
    lateinit var etPwd: EditText
    @BindView(R.id.et_pwd_repeat)
    lateinit var etPwdRepeat: EditText
    @BindView(R.id.btn_login_commit)
    lateinit var btnLoginCommit: TextView
    @BindView(R.id.ll_agreement)
    lateinit var llAgreement: LinearLayout
    private var type: Int = 0
    private var phone: String? = null

    override val contentViewResId: Int
        get() = R.layout.activity_set_pwd


    override fun initView() {
        val intent = intent
        type = intent.getIntExtra(SET_PWD_TYPE, 0)
        titleBar.leftView.setOnClickListener { finish() }
        when (type) {
            TYPE_REGISTER -> {
                titleBar.addRightButton(resources.getString(R.string.login_to_login), { activityUtil.go(LoginActivity::class.java).start() })
                llAgreement.visibility = View.VISIBLE
                btnLoginCommit.setText(R.string.login_register)
            }
            TYPE_FORGET -> {
                titleBar.addRightButton(resources.getString(R.string.back_to_login), { activityUtil.go(LoginActivity::class.java).start() })
                tvTitle.setText(R.string.find_pwd)
            }
        }
        val watcher = EditEmptyWatcher(this)
        etPwd.addTextChangedListener(watcher)
        etPwdRepeat.addTextChangedListener(watcher)
        //titleBar.setLeftButton(R.mipmap.icon_back_black, View.OnClickListener { finish() })
    }

    override fun initData() {
        phone = getIntent().getStringExtra("phone")
        Log.d("SetPwdActivity", phone)
    }

    @OnClick(R.id.btn_login_commit, R.id.tv_service_agreement)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_login_commit -> when (type) {
                TYPE_REGISTER -> if (commitCheck()) {
                    appComponent.netWork.register(phone!!, etPwd.text.toString().toMD5())
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .doOnLoading { isShowDialog(it) }
                            .subscribe { user ->
                                showToast(getString(R.string.register_ok))
                                appComponent.userHandler.saveUser(user)
                                activityUtil.go(EditTeacherInfoActivity::class.java).start()
                                finish()
                            }
                }
                TYPE_FORGET -> if (commitCheck()) {
                    appComponent.netWork.resetPwd(phone!!, etPwd.text.toString().toMD5())
                            .doOnSubscribe { mCompositeDisposable.add(it) }
                            .doOnLoading { isShowDialog(it) }
                            .subscribe { _ ->
                                showToast(getString(R.string.reset_pwd_success))
                                activityUtil.go(LoginActivity::class.java).start()
                                finish()
                            }
                }
            }
            R.id.tv_service_agreement -> activityUtil.go(H5Activity::class.java)
                    .put("url", appComponent.netWork.getH5Url(NetWork.TYPE_AGREEMENT)).start()
        }
    }

    override fun checkAllEditContent() {
        val pwd = etPwd.text.toString().trim { it <= ' ' }
        val pwdRepeat = etPwdRepeat.text.toString().trim { it <= ' ' }
        btnLoginCommit.isEnabled = TextUtils.getTrimmedLength(pwd) >= 6 && TextUtils.getTrimmedLength(pwdRepeat) >= 6
    }

    override fun commitCheck(): Boolean {
        val pwd = etPwd.text.toString().trim { it <= ' ' }
        val pwdRepeat = etPwdRepeat.text.toString().trim { it <= ' ' }
        if (pwd != pwdRepeat) {
            Toast.create(this).show("Please enter the same password two times ")
            return false
        }
        return super.commitCheck()
    }

    companion object {
        val SET_PWD_TYPE = "SET_PWD_TYPE"
        val TYPE_REGISTER = 1
        val TYPE_FORGET = 2
    }
}
