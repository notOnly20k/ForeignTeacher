package com.xld.foreignteacher.ui.mine.verification


import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.StarrBarDialog
import com.xld.foreignteacher.views.StarBarView
import kotlinx.android.synthetic.main.activity_verification.*

/**
 * Created by cz on 4/10/18.
 */
class VerificationActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_verification
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.setTitle((R.string.verification).toFormattedString(this))
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })

        tv_teaching.setOnClickListener {
            activityUtil.go(TeachingCertificateActivity::class.java).start()
        }
        tv_chinese_level.setOnClickListener {
            StarrBarDialog().apply {
                setRating(this@VerificationActivity.findViewById<StarBarView>(R.id.sbv_starbar).getSartRating())
                setDialogListene(object : StarrBarDialog.SelectStarListener {
                    override fun onOkClick(level: Float) {
                        this@VerificationActivity.findViewById<StarBarView>(R.id.sbv_starbar).setStarRating(level)
                    }

                })
            }.show(supportFragmentManager, "star_dialog")
        }

        tv_passport.setOnClickListener {
            activityUtil.go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.PASSPORT).start()
        }
        tv_visa.setOnClickListener {
            activityUtil.go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.VALID_VISA).start()
        }
    }

    override fun initData() {

    }
}