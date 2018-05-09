package com.xld.foreignteacher.ui.order.group

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import com.google.gson.Gson
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.OrderNum
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ui.order.adapter.SingleOrderFragmentAdapter
import kotlinx.android.synthetic.main.fragment_single_order.*
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/2/18.
 */
class GroupOrdersFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("GroupOrdersFragment")
    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<String>()
    private var orderNumList = mutableListOf<Int>()

    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_group_order
    }

    override fun onFirstVisibleToUser() {
        titles.add(FOR_SALE)
        titles.add(OPEN)
        titles.add(PENDING)
        titles.add(FINISHED)
        titles.add(CANCELED)
        fragmentList.add(GroupOrderListFragment.createInstance(FOR_SALE))
        fragmentList.add(GroupOrderListFragment.createInstance(OPEN))
        fragmentList.add(GroupOrderListFragment.createInstance(PENDING))
        fragmentList.add(GroupOrderListFragment.createInstance(FINISHED))
        fragmentList.add(GroupOrderListFragment.createInstance(CANCELED))

        vp_order.adapter = SingleOrderFragmentAdapter(childFragmentManager, fragmentList)
        tab_order.setupWithViewPager(vp_order)
        tab_order.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tvTab = tab.customView!!.findViewById<TextView>(R.id.tv_tab)
                tvTab.isSelected = true
                tvTab.textSize = 14F
                val tvBrage = tab.customView!!.findViewById<TextView>(R.id.barge)
                when {
                    orderNumList.isEmpty() -> tvBrage.visibility = View.GONE
                    orderNumList[tab.position] == 0 -> tvBrage.visibility = View.GONE
                    else -> {
                        tvBrage.visibility = View.VISIBLE
                        tvBrage.text = orderNumList[tab.position].toString()
                    }
                }
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
            val tab = tab_order.getTabAt(i)
            tab!!.setCustomView(R.layout.item_order_table)
            tab.customView!!.findViewById<TextView>(R.id.tv_tab).text = titles[i]
            val tvBrage = tab.customView!!.findViewById<TextView>(R.id.barge)
            when {
                orderNumList.isEmpty() -> tvBrage.visibility = View.GONE
                orderNumList[tab.position] == 0 -> tvBrage.visibility = View.GONE
                else -> {
                    tvBrage.visibility = View.VISIBLE
                    tvBrage.text = orderNumList[tab.position].toString()
                }
            }
        }
        vp_order.currentItem = 0
        tab_order.getTabAt(0)!!.select()

        initData()
    }

    private fun initData() {
        appComponent.netWork.getOrderNum(5, 2)
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    if (it != null) {
                        val orderNum = Gson().fromJson<OrderNum>(it.toString(), OrderNum::class.java)
                        orderNumList.add(orderNum.num1)
                        orderNumList.add(orderNum.num2)
                        orderNumList.add(orderNum.num3)
                        orderNumList.add(orderNum.num4)
                        orderNumList.add(orderNum.num5)
                        for (i in 0 until titles.size) {
                            val tab = tab_order.getTabAt(i)
                            val tvBrage = tab!!.customView!!.findViewById<TextView>(R.id.barge)
                            when {
                                orderNumList.isEmpty() -> tvBrage.visibility = View.GONE
                                orderNumList[tab.position] == 0 -> tvBrage.visibility = View.GONE
                                else -> {
                                    tvBrage.visibility = View.VISIBLE
                                    tvBrage.text = orderNumList[tab.position].toString()
                                }
                            }
                        }
                    }
                }
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
        const val FOR_SALE = "For sale"
        const val OPEN = "OPEN"
        const val PENDING = "Pending"
        const val FINISHED = "Finished"
        const val CANCELED = "Canceled"
    }
}
