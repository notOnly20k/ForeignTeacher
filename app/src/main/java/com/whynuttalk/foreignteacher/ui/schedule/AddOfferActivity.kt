package com.whynuttalk.foreignteacher.ui.schedule

import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.NetWork.Companion.TYPE_CHARGE_RULES
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.H5Activity
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.dialog.WheelDialog
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
    var type = -1
    var id = -1

    override fun initView() {
        type = intent.getIntExtra("type", -1)
        id = intent.getIntExtra("id", -1)
        if (type == EDIT) {
            title_bar.setTitle("Edit Offer")
        } else {
            title_bar.setTitle("Add Offer")
        }
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })

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
                                                typeId = it.id
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
                                                languageId = it.id
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
        var orderId: Int? = null
        if (type == EDIT) {
            orderId = id
        }
        appComponent.netWork.addOffer(appComponent.userHandler.getUser()!!.id, et_title.text.toString(), languageId!!, typeId!!, price, orderId)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    finish()
                }
    }


    override fun initData() {
        if (type == EDIT) {
            appComponent.netWork.getCurriculumDetail(id)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe { curriculum ->
                        et_title.setText(curriculum.title)
                        et_price.setText(curriculum.price.toString())
                        appComponent.netWork.getLanguage()
                                .doOnSubscribe { mCompositeDisposable.add(it) }
                                .subscribe { list ->
                                    list.map {
                                        if (it.id == curriculum.languagesId) {
                                            languageId = it.id
                                            et_language.setText(it.eName)
                                        }
                                    }
                                }
                        appComponent.netWork.getClassificationList()
                                .doOnSubscribe { mCompositeDisposable.add(it) }
                                .subscribe { list ->
                                    list.map {
                                        if (it.id == curriculum.languagesId)
                                            typeId = it.id
                                        et_type.setText(it.name)
                                    }
                                }
                    }

        }
        appComponent.netWork.getBenchmarkPrice(appComponent.userHandler.getUser()!!.id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    if (it.benchmarkPrice != null) {
                        val text = SpannableString("Actual income:￥${it.benchmarkPrice}/h")
                        text.setSpan(ForegroundColorSpan(resources.getColor(R.color.black_00)), 15, 15+it.benchmarkPrice.toString().length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                        tv_actual.text = text
                    } else {
                        tv_actual.visibility = View.GONE
                    }
                }
    }

    companion object {
        const val ADD = 12
        const val EDIT = 11
    }
}
