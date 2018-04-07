package com.xld.foreignteacher.ui.order.single

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.order.adapter.SingleOrderFragmentAdapter
import kotlinx.android.synthetic.main.fragment_single_order.*
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/2/18.
 */
class SingleOrderFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("SingleOrderFragment")
    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<String>()
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order
    }

    override fun onFirstVisibleToUser() {
        titles.add(NEW_ORDERS)
        titles.add(PENDING)
        titles.add(FINISHED)
        titles.add(CANCELED)
        titles.add(DECLINED)
        fragmentList.add(SingleOrderListFragment.createInstance(NEW_ORDERS))
        fragmentList.add(SingleOrderListFragment.createInstance(PENDING))
        fragmentList.add(SingleOrderListFragment.createInstance(FINISHED))
        fragmentList.add(SingleOrderListFragment.createInstance(CANCELED))
        fragmentList.add(SingleOrderListFragment.createInstance(DECLINED))


        vp_order.adapter = SingleOrderFragmentAdapter(childFragmentManager, fragmentList, titles)
        tab_order.setupWithViewPager(vp_order)
        tab_order.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tvTab = tab.customView!!.findViewById<TextView>(R.id.tv_tab)
                tvTab.isSelected = true
                tvTab.textSize = 18F
                tvTab.setTextColor(resources.getColor(R.color.yellow_ffcc00))
                vp_order.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tvTab = tab.customView!!.findViewById<TextView>(R.id.tv_tab)
                tvTab.isSelected = false
                tvTab.textSize = 12F
                tvTab.setTextColor(resources.getColor(R.color.grey_cccccc))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        for (i in 0 until titles.size) {
            logger.e { i }
            val tab = tab_order.getTabAt(i)
            tab!!.setCustomView(R.layout.item_order_table)
            tab.customView!!.findViewById<TextView>(R.id.tv_tab).text = titles[i]
        }
        vp_order.currentItem = 1
        tab_order.getTabAt(0)!!.select()


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

    companion object {
        const val NEW_ORDERS = "New orders"
        const val PENDING = "Pending"
        const val FINISHED = "Finished"
        const val CANCELED = "Canceled"
        const val DECLINED = "Declined"
    }

}
