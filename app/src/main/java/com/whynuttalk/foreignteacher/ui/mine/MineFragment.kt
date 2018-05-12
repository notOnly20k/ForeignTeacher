package com.whynuttalk.foreignteacher.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMWeb
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.ListActivity
import com.whynuttalk.foreignteacher.ui.dialog.ShareDialog
import com.whynuttalk.foreignteacher.ui.mine.about.AboutActivity
import com.whynuttalk.foreignteacher.ui.mine.invite.InviteActivity
import com.whynuttalk.foreignteacher.ui.mine.setting.SettingActivity
import com.whynuttalk.foreignteacher.ui.mine.verification.VerificationActivity
import com.whynuttalk.foreignteacher.ui.mine.wallet.WalletActivity
import com.whynuttalk.foreignteacher.ui.userinfo.TeacherDetailActivity
import kotlinx.android.synthetic.main.fragment_mine.*


/**
 * Created by cz on 3/29/18.
 */
class MineFragment : BaseFragment(), ShareDialog.OnShareItemClickListener, UMShareListener {
    override fun onStart(p0: SHARE_MEDIA?) {

    }

    override fun onResult(p0: SHARE_MEDIA?) {

    }

    override fun onCancel(p0: SHARE_MEDIA?) {

    }

    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {

    }


    private val activityUtil = ActivityUtil.create(this)
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_mine
    }

    override fun onFirstVisibleToUser() {
        mSwipeRefreshLayout.setOnRefreshListener { initDate() }
        iv_head.setOnClickListener {
            activityUtil.go(InfoActivity::class.java).put("type", InfoActivity.SHOW).start()
        }
        tv_verification.setOnClickListener {
            activityUtil.go(VerificationActivity::class.java).start()
        }

        tv_wallet.setOnClickListener {
            activityUtil.go(WalletActivity::class.java).start()
        }

        tv_invite.setOnClickListener {
            activityUtil.go(InviteActivity::class.java).start()
        }

        tv_share.setOnClickListener {
            ShareDialog().apply {
                setOnShareItemClickListener(this@MineFragment)
            }.show(childFragmentManager, "dialog_share")
        }

        tv_comment.setOnClickListener {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.COMMENT).start()
        }

        tv_about.setOnClickListener {
            activityUtil.go(AboutActivity::class.java).start()
        }
        tv_setting.setOnClickListener {
            activityUtil.go(SettingActivity::class.java).start()
        }
        tv_preview.setOnClickListener {
            activityUtil.go(TeacherDetailActivity::class.java).put("id", appComponent.userHandler.getUser().id).put("type", 1).start()
        }
        initDate()
    }

    private fun initDate() {
        val user = appComponent.userHandler.getUser()
        appComponent.netWork.getTeacherInfoSurvey(user.id)
                .doOnSubscribe { addDisposable(it) }
                .doOnLoading { mSwipeRefreshLayout?.isRefreshing = it }
                .subscribe {
                    val teacher = it.teachers!!
                    appComponent.userHandler.saveUser(user.copy(
                            imgUrl = teacher.imgUrl,
                            point = it.score?.score ?: user.point,
                            nickName = teacher.nickName ?: user.nickName,
                            inviteCode = teacher.inviteCode,
                            identCode = teacher.identCode,
                            openCityId = teacher.openCityId,
                            sex = teacher.sex,
                            phone = teacher.phone,
                            birthDay = teacher.birthDay
                    ))
                    tv_name.text = it.teachers!!.nickName
                    iv_head.setImageURI(it.teachers!!.imgUrl)
                    tv_income_count.text = it.teachers!!.lucreTotal.toString()
                    tv_order_count.text = it.ingOrderNum.toString() + "/" + it.totalOrderNum.toString()
                    tv_rating_count.text = it.score?.score?.toString() ?: "0.0"
                    tv_cancel_count.text = it.cancelOrderNum.toString()
                }
    }

    override fun share(type: SHARE_MEDIA) {
        val web = UMWeb(appComponent.netWork.DownLoadWeb)
        web.title = "This is music title"//标题
        //web.setThumb()  //缩略图
        web.description = "my description"//描述

        ShareAction(activity)
                .setPlatform(type)
                .withMedia(web)
                .setCallback(this)
                .share()
    }


    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

}
