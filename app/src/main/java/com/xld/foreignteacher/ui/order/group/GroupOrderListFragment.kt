package com.xld.foreignteacher.ui.order.group

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.timmy.tdialog.TDialog
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.GroupOrder
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.order.adapter.GroupOrderAdapter
import kotlinx.android.synthetic.main.fragment_single_order_list.*

/**
 * Created by cz on 4/8/18.
 */
class GroupOrderListFragment : BaseFragment() {
    private lateinit var type: String
    private lateinit var adpter: GroupOrderAdapter
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order_list
    }

    private val dataList = mutableListOf<GroupOrder>()
    private var page = 1

    override fun onFirstVisibleToUser() {
        dataList.clear()
        type = arguments!!.getString(FRAGMENT_TYPE, "")
        adpter = GroupOrderAdapter(context, childFragmentManager, type)
        rec_order.setAdapter(adpter)
        rec_order.setLayoutManager(LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL })
        adpter.setCallback(object : GroupOrderAdapter.OnItemClickCallback {
            override fun onDeletItem(id: Int) {
                CustomDialog.Builder()
                        .create()
                        .setTitle("Are you sure you want to delete this record?")
                        .setButton1Text("Yes")
                        .setButton2Text("No")
                        .setDialogListene(object : CustomDialog.CustomDialogListener {
                            override fun clickButton1(customDialog: CustomDialog) {

                                appComponent.netWork.delFigh(id)
                                        .doOnSubscribe { addDisposable(it) }
                                        .doOnLoading { }
                                        .subscribe {
                                            customDialog.dismiss()
                                            val list = mutableListOf<GroupOrder>()
                                            list.addAll(dataList)
                                            list.map {
                                                if (it.id == id) {
                                                    dataList.remove(it)
                                                }
                                            }
                                            adpter.setDataList(dataList)
                                            showTDialog()
                                        }
                            }

                            override fun clickButton2(customDialog: CustomDialog) {
                                customDialog.dismiss()
                            }

                        }).show(fragmentManager, "delete_order")
            }

            override fun onItemClick(id: Int) {
                ActivityUtil.create(this@GroupOrderListFragment).go(GroupDetailActivity::class.java)
                        .put("id", id)
                        .put("type", type).start()
            }

        })
        initData(1)
        rec_order.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener {
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
            GroupOrdersFragment.FOR_SALE -> {
                state = 3
            }
            GroupOrdersFragment.PENDING -> {
                state = 5
            }
            GroupOrdersFragment.FINISHED -> {
                state = 6
            }
            GroupOrdersFragment.CANCELED -> {
                state = 7
            }
            GroupOrdersFragment.OPEN -> {
                state = 4
            }
        }
        appComponent.netWork.getTeacherFightList(1, state, page, 10)
                .doOnSubscribe { addDisposable(it) }
                .doOnLoading { if (rec_order != null) rec_order.isRefreshing = it }
                .subscribe { it ->
                    dataList.addAll(it)
                    adpter.setDataList(dataList)
                    if (it.isEmpty()) {
                        this.page--
                    }

                    rec_order?.isNoMoreData(it.isEmpty())
                }
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

//        val timer: CountDownTimer = object : CountDownTimer(2000, 1000) {
//            override fun onTick(l: Long) {
//            }
//
//            override fun onFinish() {
//                dialog?.dismiss()
//            }
//        }.start()
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        adpter.clearAllDisposable()
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
