package com.xld.foreignteacher.ui.report

import android.support.v7.widget.LinearLayoutManager
import com.xld.foreignteacher.R
import com.xld.foreignteacher.data.DeclineReason
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.report.adapter.DeclinedAdapter
import kotlinx.android.synthetic.main.activity_declined.*

/**
 * Created by cz on 4/8/18.
 */
class DeclinedActivity : BaseTranslateStatusActivity() {
    private lateinit var adapter: DeclinedAdapter
    private var list = mutableListOf<DeclineReason>()
    override val contentViewResId: Int
        get() = R.layout.activity_declined
    override val changeTitleBar: Boolean
        get() = false

    override fun initView() {
        title_bar.titlelayout.setBackgroundResource(R.color.black_00)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        title_bar.setTitle("Declined")
        btn_commit.isEnabled =false

        adapter = DeclinedAdapter(this, emptyList())
        rec_reason.layoutManager = LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
        rec_reason.adapter = adapter
        adapter.setListener(object : DeclinedAdapter.OnItemSelectedListener {
            override fun onItemSelected(declineReason: DeclineReason, position: Int) {
                list.map { it.isSelect = false }
                list[position].isSelect =true
                adapter.upDateList(list)
                btn_commit.isEnabled =true
            }

        })
    }

    override fun initData() {
        list.add(DeclineReason("Sorry,this location is too far for me,please choose another then order again",false))
        list.add(DeclineReason("Sorry,too short notice,please choose another time",false))
        list.add(DeclineReason("Sorry,my offer doesn't match your request",false))
        list.add(DeclineReason("Sorry,not enough information for me to understand your request",false))
        list.add(DeclineReason("Sorry,I need to attend an emergency",false))
        adapter.upDateList(list)
    }
}
