package com.whynuttalk.foreignteacher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import com.whynuttalk.foreignteacher.R

/**
 * Created by cz on 4/11/18.
 */
class ListFragment : BaseFragment() {
    override fun getContentViewLayoutID(): Int {
       return R.layout.fragment_list
    }

    override fun onFirstVisibleToUser() {
        arguments!!.getString("type")
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
        const val MY_OFFERS = "my offers"
        const val MY_GROUP_ORDER = "my group order"
        @JvmStatic
        fun createInstance(type: String): ListFragment {
            return ListFragment().apply {
                arguments = Bundle(1).apply {
                    putString("type", type)
                }
            }
        }
    }
}
