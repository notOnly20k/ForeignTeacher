package com.xld.foreignteacher.ui.mine

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
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.ListActivity
import com.xld.foreignteacher.ui.dialog.ShareDialog
import com.xld.foreignteacher.ui.mine.about.AboutActivity
import com.xld.foreignteacher.ui.mine.invite.InviteActivity
import com.xld.foreignteacher.ui.mine.setting.SettingActivity
import com.xld.foreignteacher.ui.mine.verification.VerificationActivity
import com.xld.foreignteacher.ui.mine.wallet.WalletActivity
import com.xld.foreignteacher.ui.userinfo.EditTeacherInfoActivity
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
        iv_head.setOnClickListener {
            activityUtil.go(EditTeacherInfoActivity::class.java).put("type", EditTeacherInfoActivity.SAVE).start()
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

    }

    override fun share(type: SHARE_MEDIA) {
        val web = UMWeb("http://www.baidu.com")
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
