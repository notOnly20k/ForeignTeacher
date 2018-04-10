package com.xld.foreignteacher.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.mine.invite.InviteActivity
import com.xld.foreignteacher.ui.mine.verification.VerificationActivity
import com.xld.foreignteacher.ui.mine.wallet.WalletActivity
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by cz on 3/29/18.
 */
class MineFragment: BaseFragment() {
    private val activityUtil=ActivityUtil.create(this)
    override fun getContentViewLayoutID(): Int {
        return  R.layout.fragment_mine
    }

    override fun onFirstVisibleToUser() {
        tv_verification.setOnClickListener {
            activityUtil.go(VerificationActivity::class.java).start()
        }

        tv_wallet.setOnClickListener {
            activityUtil.go(WalletActivity::class.java).start()
        }

        tv_invite.setOnClickListener {
            activityUtil.go(InviteActivity::class.java).start()
        }
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