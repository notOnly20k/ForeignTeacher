package com.xld.foreignteacher.ui.schedule.adapter

import android.support.design.widget.TabLayout
import android.view.View
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.TimeUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.order.adapter.SingleOrderFragmentAdapter
import com.xld.foreignteacher.ui.schedule.ScheduleCardFragment
import com.xld.foreignteacher.views.ScheduleDateTextView
import kotlinx.android.synthetic.main.activity_edit_schedule.*
import java.util.*

/**
 * Created by cz on 4/17/18.
 */
class EditScheduleActivity : BaseTranslateStatusActivity(), ScheduleCardFragment.ScheduleCardFragmentCallBack {

    private val selectList = mutableListOf<Int>()
    override val contentViewResId: Int
        get() = R.layout.activity_edit_schedule
    override val changeTitleBar: Boolean
        get() = false


    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<Date>()

    override fun initView() {
        initTitle()
        initScheduleView()
    }

    private fun initTitle() {
        val type = intent.getStringExtra("type")
        if (type == DISCOUNT) {
            title_bar.setTitle(DISCOUNT)
        } else if (type == UNAVAILABLE) {
            tv_discount.visibility = View.GONE
            et_discount.visibility = View.GONE
            title_bar.setTitle(UNAVAILABLE)
        }

        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, {
            upData(type)
            finish()
        })
        title_bar.addRightButton("Save") {
            upData(type)
            finish()
        }
        title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
    }

    private fun upData(type: String) {
        if (type == DISCOUNT) {

        } else if (type == UNAVAILABLE) {
          // appComponent.netWork.setTeacherScheduleEnable()
        }
    }

    private fun initScheduleView() {
        val c = Calendar.getInstance()
        for (i in 0 until 15) {
            fragmentList.add(ScheduleCardFragment.createInstance(TimeUtils.getTimeYMD(c.time), ScheduleCardFragment.ENABLE))
            if (i == 0) {
                c.add(Calendar.DAY_OF_MONTH, 0)
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1)
            }

            titles.add(c.time)
        }

        vp_schedule.adapter = SingleOrderFragmentAdapter(supportFragmentManager, fragmentList)
        tab_date.setupWithViewPager(vp_schedule)
        tab_date.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tvDate = tab.customView?.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView?.findViewById<TextView>(R.id.tv_week)
                tvDate?.isSelected = false
                tvWeek?.isSelected = false
                tvDate?.textSize = 15F
                tvWeek?.textSize = 14F

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val tvDate = tab.customView!!.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView!!.findViewById<TextView>(R.id.tv_week)
                tvDate.isSelected = true
                tvWeek.isSelected = true
                tvDate.textSize = 16F
                tvWeek.textSize = 15F
                vp_schedule.currentItem = tab.position

            }

        })
        for (i in 0 until titles.size) {
            val tab = tab_date.getTabAt(i)
            tab!!.setCustomView(R.layout.item_tab_date)
            tab.customView!!.findViewById<TextView>(R.id.tv_datetime).text = TimeUtils.getTimeDM(titles[i])
            tab.customView!!.findViewById<TextView>(R.id.tv_week).text = TimeUtils.getWeek(titles[i])
        }



        vp_schedule.currentItem = 1
        tab_date.getTabAt(0)!!.select()

    }

    override fun scheduleClick(view: ScheduleDateTextView, checked: Boolean, id: Int) {
        if (checked) {
            view.changByState(ScheduleDateTextView.NORMAL)
        } else {
            view.changByState(ScheduleDateTextView.DISABLE)
        }
    }

    override fun initData() {
    }


    companion object {
        const val DISCOUNT = "Set DisCount"
        const val UNAVAILABLE = "Unavailable time"
    }
}
