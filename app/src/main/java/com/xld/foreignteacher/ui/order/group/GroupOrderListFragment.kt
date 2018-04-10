package com.xld.foreignteacher.ui.order.group

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.order.adapter.GroupOrderAdapter
import kotlinx.android.synthetic.main.fragment_single_order_list.*

/**
 * Created by cz on 4/8/18.
 */
class GroupOrderListFragment : BaseFragment() {
    private lateinit var adpter:GroupOrderAdapter
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order_list
    }

    override fun onFirstVisibleToUser() {
        val type=arguments!!.getString(FRAGMENT_TYPE, "")
        adpter= GroupOrderAdapter(context,childFragmentManager,type)
        rec_order.setAdapter(adpter)
        rec_order.setLayoutManager(LinearLayoutManager(context).apply { orientation= LinearLayoutManager.VERTICAL})
        adpter.setCallback(object : GroupOrderAdapter.OnItemClickCallback{
            override fun onItemClick(id: String) {
                ActivityUtil.create(this@GroupOrderListFragment).go(GroupDetailActivity::class.java).start()
            }

        })
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
        const val FRAGMENT_TYPE = "Fragment_type"

        @JvmStatic
        fun createInstance(type: String): GroupOrderListFragment {
            return GroupOrderListFragment().apply {
                arguments = Bundle(1).apply {
                    putString(GroupOrderListFragment.FRAGMENT_TYPE, type)
                }
            }
        }
    }
}
