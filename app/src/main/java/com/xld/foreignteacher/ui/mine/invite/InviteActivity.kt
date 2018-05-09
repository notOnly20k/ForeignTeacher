package com.xld.foreignteacher.ui.mine.invite

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_invite.*

/**
 * Created by cz on 4/10/18.
 */
class InviteActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_invite
    override val changeTitleBar: Boolean
        get() = true

    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }

        btn_ok.setOnClickListener {
            if (et_invite_code.text.isNotEmpty()) {
                appComponent.netWork.addInviteCode(appComponent.userHandler.getUser()!!.id, et_invite_code.text.toString())
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            ll_add_code.visibility = View.GONE
                            showToast("Add invite code success")
                        }
            }
        }
    }

    override fun initData() {

        appComponent.netWork.getUserInviteCode(appComponent.userHandler.getUser().id)
                .doOnLoading { isShowDialog(it) }
                .subscribe { it ->
                    if (it.isAddInviteCode) {
                        btn_ok.isEnabled = false
                        et_invite_code.setText(appComponent.userHandler.getUser().inviteCode)
                        et_invite_code.isEnabled = false
                        tv_invited.visibility=View.VISIBLE
                    } else {
                        btn_ok.isEnabled = true
                    }
                    tv_my_code.text = it.identCode
                    val inviteNum = SpannableString("App-Invited ${it.inviteNum} friends")
                    inviteNum.setSpan(ForegroundColorSpan(resources.getColor(R.color.yellow_ffcc00)), 12, 12 + it.inviteNum.toString().length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                    val money = SpannableString("Received ￥${it.money} cash reward")
                    money.setSpan(ForegroundColorSpan(resources.getColor(R.color.yellow_ffcc00)), 9, 9 + it.inviteNum.toString().length + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tv_invite_num.text = inviteNum
                    tv_money.text = money
                }

    }
}
