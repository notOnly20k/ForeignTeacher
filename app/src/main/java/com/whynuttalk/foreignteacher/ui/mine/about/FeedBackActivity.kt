package com.whynuttalk.foreignteacher.ui.mine.about

import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 * Created by cz on 4/13/18.
 */
class FeedBackActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_feedback
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Feedback")
        title_bar.addRightButton("Submit", {
            if (et_feedback.text.isEmpty()){
                showToast("You should write something before submit")
            }else{
                appComponent.netWork
                        .addFeedBack(et_feedback.text.toString(),appComponent.userHandler.getUser().id)
                        .doOnSubscribe { mCompositeDisposable.add(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe{ finish() }
            }
        })
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))

    }

    override fun initData() {

    }
}
