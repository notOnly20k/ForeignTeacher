package com.whynuttalk.foreignteacher.ui.mine.verification

import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.toFormattedString
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.mine.verification.adapter.TeacherCertificateAdapter
import kotlinx.android.synthetic.main.activity_teaching_certificate.*

/**
 * Created by cz on 4/10/18.
 */
class TeachingCertificateActivity : BaseTranslateStatusActivity() {

    override val contentViewResId: Int
        get() = R.layout.activity_teaching_certificate
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var adapter: TeacherCertificateAdapter
    override fun initView() {
        adapter = TeacherCertificateAdapter(this)
        title_bar.setTitle((R.string.teaching_certificate).toFormattedString(this))
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        rec_certificate.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayout.VERTICAL }
        rec_certificate.adapter = adapter
        swip.setOnRefreshListener { initData() }
    }

    override fun initData() {
        appComponent.netWork.getAuthenticationDetail(appComponent.userHandler.getUser().id, null, 1)
                .doOnLoading { swip?.isRefreshing = it }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    adapter.setData(it)
                }
    }
}
