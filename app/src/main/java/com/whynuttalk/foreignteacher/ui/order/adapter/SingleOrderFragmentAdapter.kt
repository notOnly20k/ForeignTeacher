package com.whynuttalk.foreignteacher.ui.order.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.sinata.xldutils.fragment.BaseFragment

/**
 * Created by cz on 4/3/18.
 */
class SingleOrderFragmentAdapter(val fm: FragmentManager, val list: List<BaseFragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

}
