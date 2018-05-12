package com.whynuttalk.foreignteacher.ui.mine.wallet

import android.graphics.Paint
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.ListActivity
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_wallet.*

/**
 * Created by cz on 4/10/18.
 */
class WalletActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_wallet
    override val changeTitleBar: Boolean
        get() = false
    var money="0"
    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.addRightButton(ListActivity.TRANSACTIONS, {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.TRANSACTIONS).start()
        })
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        tv_withdraw_details.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_withdraw_details.paint.isAntiAlias = true
        tv_withdraw_details.setOnClickListener {
            activityUtil.go(ListActivity::class.java).put("type", ListActivity.WITHDRAWDETAIL).start()
        }

        tv_with_draw.setOnClickListener {
            activityUtil.go(WithDrawActivity::class.java).put("money",money).start()
        }

    }

    override fun initData() {
        appComponent.netWork.getMyWallet(appComponent.userHandler.getUser().id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    money=it.teachers?.balance?.toString() ?: "0"
                    tv_money.text = it.teachers?.balance?.toString() ?: "0"
                    tv_monthly_income_count.text = it.lucreMonth.toString()
                    tv_weekly_average_count.text = it.weeklyAverage.toString()
                    tv_total_income_count.text = it.lucreTotal.toString()

                }
    }
}
