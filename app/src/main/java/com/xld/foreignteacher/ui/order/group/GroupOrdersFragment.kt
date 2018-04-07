package com.xld.foreignteacher.ui.order.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/2/18.
 */
class GroupOrdersFragment: BaseFragment() {
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_group_order
    }

    override fun onFirstVisibleToUser() {

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