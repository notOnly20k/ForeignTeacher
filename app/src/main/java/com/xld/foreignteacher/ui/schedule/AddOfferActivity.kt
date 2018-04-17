package com.xld.foreignteacher.ui.schedule

import android.graphics.Paint
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.NetWork.Companion.TYPE_CHARGE_RULES
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ui.H5Activity
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.WheelDialog
import kotlinx.android.synthetic.main.activity_add_offer.*

/**
 * Created by cz on 4/16/18.
 */
class AddOfferActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_add_offer
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var wheelDialog: WheelDialog

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Add Offer")
        title_bar.addRightButton("Submit", {})
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        wheelDialog = WheelDialog.Builder().create()
        val list = mutableListOf<String>()
        list.add("English")
        list.add("Chinese")
        list.add("French")
        list.add("Japanese")
        et_language.setOnClickListener {
            wheelDialog
                    .setDate(list)
                    .setListener(object : WheelDialog.WheelDialogListener {
                        override fun clickCancel() {

                        }

                        override fun clickSure(string: String) {
                            et_language.setText(string)
                        }

                    })
                    .show(supportFragmentManager, "language")
        }
        et_type.setOnClickListener {
            wheelDialog
                    .setDate(list)
                    .setListener(object : WheelDialog.WheelDialogListener {
                        override fun clickCancel() {

                        }

                        override fun clickSure(string: String) {
                            et_type.setText(string)
                        }

                    })
                    .show(supportFragmentManager, "language")
        }

        tv_charging.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_charging.paint.isAntiAlias = true
        tv_charging.setOnClickListener {
            activityUtil.go(H5Activity::class.java).put("url",appComponent.netWork.getH5Url(TYPE_CHARGE_RULES)).start()
        }


    }

    override fun initData() {
    }
}
