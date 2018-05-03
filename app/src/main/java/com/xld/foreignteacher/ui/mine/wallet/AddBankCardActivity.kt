package com.xld.foreignteacher.ui.mine.wallet

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_add_bank_card.*

/**
 * Created by cz on 4/27/18.
 */
class AddBankCardActivity: BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_add_bank_card
    override val changeTitleBar: Boolean
        get() =false

    override fun initView() {
        title_bar.setTitle("Add bank card")
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        val user =appComponent.userHandler.getUser()
        btn_add.setOnClickListener {
            if (commitCheck()) {
                appComponent.netWork.addBankAccount(user.id, et_cardholder.text.toString(), et_card_no.getTextWithoutSpace(), 2)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { showProgress(it) }
                        .subscribe { finish() }
            }
        }
        val emptyWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                checkAllEditContext()
            }
        }
        et_card_no.addTextChangedListener(emptyWatcher)
        et_cardholder.addTextChangedListener(emptyWatcher)
    }

    private fun checkAllEditContext() {
        val cn = et_card_no.text.toString().trim { it <= ' ' }
        val ch = et_cardholder.text.toString().trim()
        btn_add.isEnabled = TextUtils.getTrimmedLength(cn) == 23 && TextUtils.getTrimmedLength(ch) >0
    }

    override fun commitCheck(): Boolean {
        if (et_cardholder.text.isEmpty()){
            showToast("empty name")
            return false
        }
        if (et_card_no.getTextWithoutSpace().length!=19){
            showToast("wrong card number")
            return false
        }
        return super.commitCheck()
    }

    override fun initData() {
    }
}