package com.xld.foreignteacher.ui.mine.wallet

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.ListActivity
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
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton(ListActivity.TRANSACTIONS, {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.TRANSACTIONS).start()
        })
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))

        tv_withdraw_details.setOnClickListener {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.WITHDRAWDETAIL).start()
        }

        tv_with_draw.setOnClickListener {
            activityUtil.go(WithDrawActivity::class.java).start()
        }

    }

    override fun initData() {

    }
}
