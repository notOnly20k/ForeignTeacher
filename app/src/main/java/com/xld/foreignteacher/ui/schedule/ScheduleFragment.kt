package com.xld.foreignteacher.ui.schedule

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.ListFragment
import com.xld.foreignteacher.ui.dialog.CustomPopWindow
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.slf4j.LoggerFactory


/**
 * Created by cz on 3/29/18.
 */
class ScheduleFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("ScheduleFragment")
    private lateinit var myOfferFragment: ListFragment
    private lateinit var groupOfferFragment: ListFragment
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_schedule
    }

    override fun onFirstVisibleToUser() {
        initTab()
    }

    private fun initTab() {
        tv_title_right.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pop_schedule, null, false)
            view.findViewById<TextView>(R.id.tv_offer).setOnClickListener {

            }
            val popWindow = CustomPopWindow.PopupWindowBuilder(context)
                    .setView(view)
                    .setFocusable(true)
                    .enableBackgroundDark(true)
                    .setBgDarkAlpha(40F)
                    .setOutsideTouchable(true)
                    .create()
            popWindow.showAsDropDown(tv_title_right, 0, 10)

        }
        myOfferFragment = ListFragment.createInstance(ListFragment.MY_OFFERS)
        groupOfferFragment = ListFragment.createInstance(ListFragment.MY_GROUP_ORDER)

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
                val transaction = childFragmentManager.beginTransaction()
                if (!myOfferFragment.isAdded) {
                    transaction.add(R.id.fl_replace, myOfferFragment)
                }
                if (!myOfferFragment.isVisible) {
                    logger.e { "show..........." }
                    transaction.hide(groupOfferFragment)
                            .show(myOfferFragment).commit()
                }
            }
        }

        (rb_my_group_offer as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (groupOfferFragment.isVisible) {
                    return@setOnCheckedChangeListener
                }
                childFragmentManager.beginTransaction()
                        .hide(myOfferFragment)
                        .show(groupOfferFragment).commit()

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
