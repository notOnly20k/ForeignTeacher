package com.xld.foreignteacher.ui.msg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.schedule.adapter.ScheduleDateAdapter
import com.xld.foreignteacher.views.ScheduleLayoutManager
import com.xld.foreignteacher.views.SwipeCardRecyclerView
import kotlinx.android.synthetic.main.fragment_message.*

/**
 * Created by cz on 3/29/18.
 */
class MessageFragment: BaseFragment() {
    override fun getContentViewLayoutID(): Int {
        return  R.layout.fragment_message
    }

    override fun onFirstVisibleToUser() {
        rec_swipe.layoutManager = ScheduleLayoutManager()
        rec_swipe.adapter = ScheduleDateAdapter(context)
        rec_swipe.isNestedScrollingEnabled = false
        rec_swipe.setRemovedListener(object : SwipeCardRecyclerView.ItemRemovedListener{
            override fun onRightRemoved() {

            }

            override fun onLeftRemoved() {

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
}