package com.xld.foreignteacher.ui.mine.verification

import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_up_load_id.*

/**
 * Created by cz on 4/10/18.
 */
class UpLoadIdActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_up_load_id
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        val type = intent.getStringExtra("type")
        if (type == PASSPORT) {
            title_bar.setTitle((R.string.passport).toFormattedString(this))
        } else if (type == VALID_VISA) {
            title_bar.setTitle((R.string.valid_visa).toFormattedString(this))
        }

        title_bar.titlelayout.setBackgroundResource(R.color.black_00)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton("Submit") { finish() }
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
    }

    override fun initData() {

    }

    companion object {
        const val PASSPORT = "passport"
        const val VALID_VISA = "valid_visa"
    }
}
