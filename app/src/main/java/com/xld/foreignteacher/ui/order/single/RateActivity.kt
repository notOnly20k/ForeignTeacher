package com.xld.foreignteacher.ui.order.single

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_rate.*

/**
 * Created by cz on 4/12/18.
 */
class RateActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_rate
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.setTitle("Rate")
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })

        val backDra = resources.getDrawable(R.mipmap.icon_woman)
        backDra.setBounds(backDra.minimumWidth, 0, 0, backDra.minimumHeight)
        tv_name.setCompoundDrawablesWithIntrinsicBounds(null, null, backDra, null)
        tv_name.compoundDrawablePadding = backDra.minimumHeight / 2
    }

    override fun initData() {

    }
}