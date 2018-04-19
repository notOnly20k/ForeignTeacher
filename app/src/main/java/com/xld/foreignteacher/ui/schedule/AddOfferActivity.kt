package com.xld.foreignteacher.ui.schedule

import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import cn.sinata.xldutils.utils.SPUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.NetWork.Companion.TYPE_CHARGE_RULES
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
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
    private var languageId: Int? = null
    private var typeId: Int? = null

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Add Offer")
        title_bar.addRightButton("Submit", { submit() })
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        et_type.setOnClickListener {
            appComponent.netWork.getClassificationList()
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { list ->
                        val textList = mutableListOf<String>()
                        list.map { textList.add(it.name) }
                        WheelDialog.Builder().create()
                                .setDate(textList)
                                .setListener(object : WheelDialog.WheelDialogListener {
                                    override fun clickCancel() {

                                    }

                                    override fun clickSure(string: String) {
                                        list.map {
                                            if (it.name == string)
                                                languageId = it.id
                                        }
                                        et_type.setText(string)
                                    }

                                })
                                .show(supportFragmentManager, "type")
                    }
        }
        et_language.setOnClickListener {
            appComponent.netWork.getLanguage()
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { list ->
                        val textList = mutableListOf<String>()
                        list.map { textList.add(it.eName!!) }
                        WheelDialog.Builder().create()
                                .setDate(textList)
                                .setListener(object : WheelDialog.WheelDialogListener {
                                    override fun clickCancel() {

                                    }

                                    override fun clickSure(string: String) {
                                        list.map {
                                            if (it.eName == string)
                                                typeId = it.id
                                        }
                                        et_language.setText(string)
                                    }

                                })
                                .show(supportFragmentManager, "language")
                    }
        }

        tv_charging.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_charging.paint.isAntiAlias = true
        tv_charging.setOnClickListener {
            activityUtil.go(H5Activity::class.java).put("url", appComponent.netWork.getH5Url(TYPE_CHARGE_RULES)).start()
        }


    }

    fun submit() {
        if (typeId == null) {
            showToast("Please select type")
            return
        }
        if (languageId == null) {
            showToast("Please select language")
            return
        }

        if (et_title.text.isEmpty()) {
            showToast("Please pick title")
            return
        }
        if (et_price.text.isEmpty()) {
            showToast("Please pick price")
            return
        }
        val price = et_price.text.toString().toInt()
        logger.e { SPUtils.getInt("id") }
        appComponent.netWork.addOffer(SPUtils.getInt("id"), et_title.text.toString(), languageId!!, typeId!!, price)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { showProgress(it) }
                .subscribe {
                    finish()
                }
    }

    override fun initData() {
        appComponent.netWork.getBenchmarkPrice(SPUtils.getInt("id"))
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    val text = SpannableString("Actual income:￥${it[0].benchmarkPrice}/h")
                    text.setSpan(ForegroundColorSpan(resources.getColor(R.color.black_00)), 15, it[0].benchmarkPrice.toString().length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    tv_actual.text = text
                }
    }
}
