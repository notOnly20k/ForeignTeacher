package com.xld.foreignteacher.ui.mine.wallet

import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherBankAccout
import com.xld.foreignteacher.api.dto.User
import com.xld.foreignteacher.ext.*
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.WheelDialog
import kotlinx.android.synthetic.main.activity_with_draw.*

/**
 * Created by cz on 4/10/18.
 */
class WithDrawActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_with_draw
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var user: User
    private val cardList = mutableListOf<TeacherBankAccout>()
    private var cardId = -1

    override fun initView() {
        title_bar.setTitle((R.string.with_draw).toFormattedString(this))
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        user = appComponent.userHandler.getUser()
        tv_bank.setOnClickListener {
            appComponent.netWork.queryTeacherBankAccout(user.id)
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .logErrorAndForget {
                        it.toast()
                        if (it is ServerException) {
                            activityUtil.go(AddBankCardActivity::class.java).start()
                        }
                    }
                    .subscribe {
                        cardList.clear()
                        cardList.addAll(it)
                        val list = mutableListOf<String>()
                        it.map { list.add(it.bankName ?: "" + "  " + it.account) }
                        WheelDialog.Builder().create()
                                .setRightText("+Add bank card")
                                .setDate(list)
                                .setListener(object : WheelDialog.WheelDialogListener {
                                    override fun clickCancel() {
                                        activityUtil.go(AddBankCardActivity::class.java).start()
                                    }

                                    override fun clickSure(string: String) {
                                        it.map {
                                            if ((it.bankName ?: "" + "  " + it.account) == string) {
                                                cardId = it.id
                                            }
                                        }
                                        tv_bank_edit.text = string.formateToCard()
                                    }

                                }).show(supportFragmentManager, "wheel")
                    }

        }
        btn_with_draw.setOnClickListener {
            if (commitCheck()) {
                logger.e { "cardId =$cardId" }
                logger.e { "money =${et_amount.text.toString().toDouble()}" }
                appComponent.netWork.applyWithdraw(user.id, cardId, et_amount.text.toString().toDouble())
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { showProgress(it) }
                        .subscribe { finish() }
            }
        }
    }

    override fun commitCheck(): Boolean {
        if (tv_bank.text.isEmpty()) {
            return false
        }
        if (et_amount.text.isEmpty()) {
            return false
        }
        return super.commitCheck()
    }

    override fun initData() {

    }
}
