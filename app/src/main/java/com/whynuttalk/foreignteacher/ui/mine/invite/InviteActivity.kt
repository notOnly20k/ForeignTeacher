package com.whynuttalk.foreignteacher.ui.mine.invite

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.dialog.ShareDialog
import kotlinx.android.synthetic.main.activity_invite.*

/**
 * Created by cz on 4/10/18.
 */
class InviteActivity : BaseTranslateStatusActivity(), ShareDialog.OnShareItemClickListener, UMShareListener {
    override fun onStart(p0: SHARE_MEDIA?) {

    }

    override fun share(type: SHARE_MEDIA) {
        val web = UMWeb(appComponent.netWork.TeacherInvite)
        web.title = "This is music title"//标题
        //web.setThumb()  //缩略图
        web.description = "my description"//描述

        ShareAction(this)
                .setPlatform(type)
                .withMedia(web)
                .setCallback(this)
                .share()
    }

    override fun onResult(p0: SHARE_MEDIA?) {
    }

    override fun onCancel(p0: SHARE_MEDIA?) {
    }

    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
    }

    override val contentViewResId: Int
        get() = R.layout.activity_invite
    override val changeTitleBar: Boolean
        get() = true

    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }

        btn_invite_friend.setOnClickListener {
            ShareDialog().apply {
                setOnShareItemClickListener(this@InviteActivity)
            }.show(supportFragmentManager, "dialog_share")
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

        appComponent.netWork.getTeacherInviteCode(appComponent.userHandler.getUser().id)
                .doOnLoading { isShowDialog(it) }
                .subscribe { it ->
                    if (!it.isAddInviteCode) {
                        btn_ok.isEnabled = false
                        et_invite_code.setText(it.name)
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
