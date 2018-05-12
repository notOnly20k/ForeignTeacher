package com.whynuttalk.foreignteacher.ui.mine.setting

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import cn.sinata.xldutils.utils.Toast
import cn.sinata.xldutils.view.TitleBar
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.toMD5
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.views.EditEmptyWatcher

/**
 * Created by cz on 4/13/18.
 */
class ResetPwdActivity : BaseTranslateStatusActivity(), EditEmptyWatcher.Checkable {
    override val contentViewResId: Int
        get() = R.layout.activity_reset_pwd
    override val changeTitleBar: Boolean
        get() = false

    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.et_pwd)
    lateinit var etPwd: EditText
    @BindView(R.id.et_pwd_repeat)
    lateinit var etPwdRepeat: EditText
    @BindView(R.id.btn_login_commit)
    lateinit var btnLoginCommit: TextView
    private var type: Int = 0
    private var phone: String? = null

    override fun initView() {
        val watcher = EditEmptyWatcher(this)
        etPwd.addTextChangedListener(watcher)
        etPwdRepeat.addTextChangedListener(watcher)
        titleBar.titlelayout.setBackgroundResource(R.color.black_00)
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.back_yellow, { finish() })
        titleBar.setTitle("Change Password")
    }

    override fun initData() {
        phone = getIntent().getStringExtra("phone")
    }

    @OnClick(R.id.btn_login_commit)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btn_login_commit -> if (commitCheck()) {
                appComponent.netWork.resetPwd(phone!!, etPwd.text.toString().toMD5())
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe { _ ->
                            showToast(getString(R.string.reset_pwd_success))
                            activityUtil.go(SettingActivity::class.java).start()
                            finish()
                        }
            }
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

}
