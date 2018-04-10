package com.xld.foreignteacher.ui.mine.wallet

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_with_draw.*

/**
 * Created by cz on 4/10/18.
 */
class WithDrawActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_with_draw
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.setTitle((R.string.with_draw).toFormattedString(this))
        title_bar.titlelayout.setBackgroundResource(R.color.black_00)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
    }

    override fun initData() {

    }
}