package com.xld.foreignteacher.ui.order.single

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.PersonalTrainingOrder
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ui.order.adapter.SingleOrderAdapter
import kotlinx.android.synthetic.main.fragment_single_order_list.*
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/3/18.
 */
class SingleOrderListFragment : BaseFragment(), SingleOrderAdapter.SingleOrderItemClickListener {

    private lateinit var adpter: SingleOrderAdapter
    private val logger = LoggerFactory.getLogger("SingleOrderListFragment")
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order_list
    }
    private val dataList = mutableListOf<PersonalTrainingOrder.RowsBean>()
    private var page = 1

    private lateinit var type: String

    override fun onFirstVisibleToUser() {
        type = arguments!!.getString(SingleOrderListFragment.FRAGMENT_TYPE, "")
        adpter = SingleOrderAdapter(context, childFragmentManager, type)
        adpter.setListener(this)
        rec_order.setAdapter(adpter)
        rec_order.setLayoutManager(LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL })
        initData(1)
        rec_order.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener{
            override fun onRefresh() {
                page = 1
                dataList.clear()
                initData(page)
            }

            override fun onLoadMore() {
                page++
                initData(page)
            }

        })
    }

    private fun initData(page: Int) {
        var state = 1
        when (type) {
            SingleOrderFragment.NEW_ORDERS -> {
                state = 1
            }
            SingleOrderFragment.PENDING -> {
                state = 2
            }
            SingleOrderFragment.FINISHED -> {
                state = 10
            }
            SingleOrderFragment.CANCELED -> {
                state = 20
            }
            SingleOrderFragment.DECLINED -> {
                state = 3
            }
        }
        appComponent.netWork.getMyPersonalTrainingOrder(1, state, page, 10)
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    dataList.addAll(it.rows!!)
                    adpter.setDataList(dataList)
                    if (it.rows==null||it.rows!!.isEmpty()) {
                        this.page--
                    }

                    rec_order?.isNoMoreData(it.rows==null||it.rows!!.isEmpty())
                }
    }

    override fun onVisibleToUser() {

    }

    override fun onInvisibleToUser() {

    }

    override fun onDialogClick(type: String) {
        showTDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

    fun showTDialog() {
        val dialog = TDialog.Builder(fragmentManager)
                .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                .setScreenWidthAspect(activity, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(activity, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("SubscribeDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelable(true)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示

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
