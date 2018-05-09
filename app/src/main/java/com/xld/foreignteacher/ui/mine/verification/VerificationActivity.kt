package com.xld.foreignteacher.ui.mine.verification


import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.MyAuthentication
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.toFormattedString
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.StarrBarDialog
import com.xld.foreignteacher.views.StarBarView
import kotlinx.android.synthetic.main.activity_verification.*

/**
 * Created by cz on 4/10/18.
 */
class VerificationActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_verification
    override val changeTitleBar: Boolean
        get() = false
    val user:User by lazy { appComponent.userHandler.getUser() }
    private var myAuthentication: MyAuthentication?=null

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

                        appComponent.netWork.editTeacher(user.id, user.nickName, sex = user.sex, chineseLevel = level.toInt(),
                                birthDay = user.birthDay ?: "", contactInformation = user.phone ?: "")
                                .doOnLoading { isShowDialog(it) }
                                .subscribe { this@VerificationActivity.findViewById<StarBarView>(R.id.sbv_starbar).setStarRating(level) }
                    }

                })
            }.show(supportFragmentManager, "star_dialog")
        }

        tv_passport.setOnClickListener {
            activityUtil.go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.PASSPORT).put("data",myAuthentication).start()
        }
        tv_visa.setOnClickListener {
            activityUtil.go(UpLoadIdActivity::class.java).put("type", UpLoadIdActivity.VALID_VISA).put("data",myAuthentication).start()
        }
    }

    override fun initData() {
        appComponent.netWork.myAuthentication(appComponent.userHandler.getUser().id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    myAuthentication=it
                    tv_passport_state.text=when(it.huzhao?.state){
                        1->{"Await approval"}
                        2->{"Certification"}
                        3->{"Authenticated"}
                        0->{"Refused"}
                        else->{"Unauthorized"}
                    }
                    tv_visa_state.text=when(it.qianzheng?.state){
                        1->{"Await approval"}
                        2->{"Certification"}
                        3->{"Authenticated"}
                        0->{"Refused"}
                        else->{"Unauthorized"}
                    }
                    tv_natvie_state.text=when(it.muyu){
                        1->{"Await approval"}
                        2->{"Certification"}
                        3->{"Authenticated"}
                        0->{"Refused"}
                        else->{"Unauthorized"}
                    }

                    sbv_starbar.setStarRating(it.hanyu.toFloat())
                }
    }
}
