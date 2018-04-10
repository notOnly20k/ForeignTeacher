package com.xld.foreignteacher.ui.mine.wallet

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_wallet.*

/**
 * Created by cz on 4/10/18.
 */
class WalletActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_wallet
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        tv_with_draw.setOnClickListener {
            activityUtil.go(WithDrawActivity::class.java).start()
        }

    }

    override fun initData() {

    }
}
