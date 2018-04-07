package com.xld.foreignteacher.ui.order.single

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.order.adapter.SingleOrderAdapter
import kotlinx.android.synthetic.main.fragment_single_order_list.*

/**
 * Created by cz on 4/3/18.
 */
class SingleOrderListFragment: BaseFragment() {
    private lateinit var adpter:SingleOrderAdapter
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order_list
    }

    override fun onFirstVisibleToUser() {
        val type=arguments!!.getString(SingleOrderListFragment.FRAGMENT_TYPE, "")
        adpter=SingleOrderAdapter(context,childFragmentManager,type)
        rec_order.setAdapter(adpter)
        rec_order.setLayoutManager(LinearLayoutManager(context).apply { orientation= LinearLayoutManager.VERTICAL})
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
        fun createInstance(type: String): SingleOrderListFragment {
            return SingleOrderListFragment().apply {
                arguments = Bundle(1).apply {
                    putString(SingleOrderListFragment.FRAGMENT_TYPE, type)
                }
            }
        }
    }
}