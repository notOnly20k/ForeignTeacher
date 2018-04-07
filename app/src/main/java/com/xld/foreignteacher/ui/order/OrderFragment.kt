package com.xld.foreignteacher.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.order.group.GroupOrdersFragment
import com.xld.foreignteacher.ui.order.single.SingleOrderFragment
import kotlinx.android.synthetic.main.fragment_order.*
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Created by cz on 3/29/18.
 */
class OrderFragment : BaseFragment() {
    private val mFragmentList = ArrayList<BaseFragment>()
    private var previousPosition = 1
    private var currentPosition = 0
    val logger = LoggerFactory.getLogger("OrderFragment")
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_order
    }

    override fun onFirstVisibleToUser() {
        mFragmentList.add(SingleOrderFragment())
        mFragmentList.add(GroupOrdersFragment())
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, mFragmentList[0]).commit()
        val backDra = resources.getDrawable(R.mipmap.sport_yellow)
        backDra.setBounds(0, 0, backDra.minimumHeight, backDra.minimumHeight)

        (rb_orders as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                previousPosition = 0
                currentPosition = 1
                buttonView.setTextAppearance(context, R.style.radio_order_selected)
                buttonView.setCompoundDrawables(null, null, null, backDra)
                changeFragment(previousPosition, currentPosition)
            } else {
                buttonView.setTextAppearance(context, R.style.radio_order_normal)
                buttonView.setCompoundDrawables(null, null, null, null)
            }
        }

        (rb_group_orders as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                previousPosition = 1
                currentPosition = 0
                buttonView.setTextAppearance(context, R.style.radio_order_selected)
                buttonView.setCompoundDrawables(null, null, null, backDra)
                changeFragment(previousPosition, currentPosition)

            } else {
                buttonView.setTextAppearance(context, R.style.radio_order_normal)
                buttonView.setCompoundDrawables(null, null, null, null)
            }
        }

        (rb_orders as RadioButton).isChecked = true

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


    private fun changeFragment(previousPosition: Int, currentPosition: Int) {
        val transaction = childFragmentManager.beginTransaction()
        if (currentPosition != previousPosition) {
            transaction.hide(mFragmentList[currentPosition])
            if (!mFragmentList[currentPosition].isAdded) {
                transaction.add(R.id.fl_content, mFragmentList[currentPosition])
            }
            transaction.show(mFragmentList[previousPosition]).commit()
        }
    }
}
