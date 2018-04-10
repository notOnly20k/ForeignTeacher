package com.xld.foreignteacher.ui.mine.invite

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_invite.*

/**
 * Created by cz on 4/10/18.
 */
class InviteActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_invite
    override val changeTitleBar: Boolean
        get() = true

    override fun initView() {
        iv_back.setOnClickListener {
            finish()
        }
    }

    override fun initData() {

    }
}