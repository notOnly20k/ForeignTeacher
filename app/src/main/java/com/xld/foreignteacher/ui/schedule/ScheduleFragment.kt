package com.xld.foreignteacher.ui.schedule

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.dialog.CustomPopWindow
import com.xld.foreignteacher.ui.schedule.adapter.MyGroupOfferAdapter
import com.xld.foreignteacher.ui.schedule.adapter.MyOfferAdapter
import com.xld.foreignteacher.ui.schedule.adapter.ScheduleDateAdapter
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.slf4j.LoggerFactory


/**
 * Created by cz on 3/29/18.
 */
class ScheduleFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("ScheduleFragment")
    //private val activityUtil = ActivityUtil.create(context)
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_schedule
    }

    override fun onFirstVisibleToUser() {
        initTab()
    }

    private lateinit var myOfferAdapter: MyOfferAdapter
    private lateinit var myGroupOfferAdapter: MyGroupOfferAdapter
    private lateinit var popWindow: CustomPopWindow

    private fun initTab() {
        myGroupOfferAdapter = MyGroupOfferAdapter(context)
        myOfferAdapter = MyOfferAdapter(context)
        rec_content.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayout.VERTICAL }
        tv_title_right.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pop_schedule, null, false)
            view.findViewById<TextView>(R.id.tv_offer).setOnClickListener {
                startActivity(Intent(activity, AddOfferActivity::class.java))
                popWindow.dissmiss()
            }
            view.findViewById<TextView>(R.id.tv_group).setOnClickListener {
                startActivity(Intent(activity, AddGroupOfferActivity::class.java))
                popWindow.dissmiss()
            }
            popWindow = CustomPopWindow.PopupWindowBuilder(context)
                    .setView(view)
                    .setFocusable(true)
                    .enableBackgroundDark(true)
                    .setBgDarkAlpha(40F)
                    .setOutsideTouchable(true)
                    .create()
            popWindow.showAsDropDown(tv_title_right, 0, 10)

        }

        rec_schedule.layoutManager = LinearLayoutManager(context).apply { orientation = LinearLayout.HORIZONTAL }
        rec_schedule.adapter = ScheduleDateAdapter(context).apply {
            setListener(object : ScheduleDateAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    tab_date.getTabAt(position)!!.select()
                }

            })
        }
        rec_schedule.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) {
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })
        rec_schedule.isNestedScrollingEnabled = false

        for (i in 1..13) {
            val tab = tab_date.newTab().setCustomView(R.layout.item_tab_date)
            tab_date.addTab(tab, i - 1)
        }
        tab_date.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tvDate = tab.customView!!.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView!!.findViewById<TextView>(R.id.tv_week)
                tvDate.isSelected = false
                tvWeek.isSelected = false
                tvDate.textSize = 15F
                tvWeek.textSize = 14F

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                rec_schedule.scrollToPosition(tab.position)
                val tvDate = tab.customView!!.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView!!.findViewById<TextView>(R.id.tv_week)
                tvDate.isSelected = true
                tvWeek.isSelected = true
                tvDate.textSize = 16F
                tvWeek.textSize = 15F

            }

        })
        (rb_my_offer as RadioButton).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                img_line_right.visibility = View.VISIBLE
                img_line_left.visibility = View.VISIBLE
                rec_content.adapter = myOfferAdapter
            }
        }

        (rb_my_group_offer as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                img_line_right.visibility = View.GONE
                img_line_left.visibility = View.GONE
                rec_content.adapter = myGroupOfferAdapter
            }
        }

        (rb_my_offer as RadioButton).isChecked = true

    }

    override fun onVisibleToUser() {

    }

    override fun onInvisibleToUser() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

}
