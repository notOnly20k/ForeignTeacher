package com.xld.foreignteacher.ui.schedule

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_add_offer.*

/**
 * Created by cz on 4/16/18.
 */
class AddOfferActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_add_offer
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Add Offer")
        title_bar.addRightButton("Submit", {})
    }

    override fun initData() {
    }
}
