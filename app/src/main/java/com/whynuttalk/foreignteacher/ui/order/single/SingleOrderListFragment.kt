package com.whynuttalk.foreignteacher.ui.order.single

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import com.amap.api.navi.INaviInfoCallback
import com.amap.api.navi.model.AMapNaviLocation
import com.timmy.tdialog.TDialog
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.PersonalTrainingOrder
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.order.adapter.SingleOrderAdapter
import kotlinx.android.synthetic.main.fragment_single_order_list.*
import org.slf4j.LoggerFactory


/**
 * Created by cz on 4/3/18.
 */
class SingleOrderListFragment : BaseFragment(), SingleOrderAdapter.SingleOrderItemClickListener, INaviInfoCallback {
    override fun onGetNavigationText(p0: String?) {
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
    }

    override fun onInitNaviFailure() {
    }

    override fun onStrategyChanged(p0: Int) {
    }

    override fun onReCalculateRoute(p0: Int) {
    }

    override fun getCustomNaviView(): View {
        return customNaviView
    }

    override fun onCalculateRouteFailure(p0: Int) {
    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
    }

    override fun getCustomNaviBottomView(): View {
        return customNaviBottomView
    }

    override fun onArrivedWayPoint(p0: Int) {
    }

    override fun onArriveDestination(p0: Boolean) {
    }

    override fun onStartNavi(p0: Int) {
    }

    override fun onStopSpeaking() {
    }

    override fun onExitPage(p0: Int) {
    }

    private lateinit var adpter: SingleOrderAdapter
    private val logger = LoggerFactory.getLogger("SingleOrderListFragment")
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_single_order_list
    }

    private val dataList = mutableListOf<PersonalTrainingOrder.RowsBean>()
    private var page = 1

    private lateinit var type: String

    override fun onFirstVisibleToUser() {
        dataList.clear()
        type = arguments!!.getString(SingleOrderListFragment.FRAGMENT_TYPE, "")
        adpter = SingleOrderAdapter(context, childFragmentManager, type)
        adpter.setListener(this)
        rec_order.setAdapter(adpter)
        rec_order.setLayoutManager(LinearLayoutManager(context).apply { orientation = LinearLayoutManager.VERTICAL })
        initData(1)
        rec_order.loadMoreTrans()
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

    override fun loacte(address: String) {
        AmapNaviPage.getInstance().showRouteActivity(context, AmapNaviParams(null, null, Poi(" Jiangxin Mr. Axe Restaurant (Zhonghai Huanyuhui Shop)", LatLng(30.58107,104.056211),null), AmapNaviType.DRIVER),this)
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
        appComponent.netWork.getMyPersonalTrainingOrder(appComponent.userHandler.getUser().id, state, page, 10)
                .doOnSubscribe { addDisposable(it) }
                .doOnLoading { rec_order?.isRefreshing = it }
                .subscribe {
                    dataList.addAll(it.rows!!)
                    adpter.setDataList(dataList)
                    if (it.rows == null || it.rows!!.isEmpty()) {
                        this.page--
                    }

                    rec_order?.isNoMoreData(it.rows == null || it.rows!!.isEmpty())
                }
    }

    override fun onVisibleToUser() {

    }

    override fun onInvisibleToUser() {

    }

    override fun onDialogClick(type: String, id: Int) {
        when (type) {
            SingleOrderFragment.DECLINED, SingleOrderFragment.CANCELED, SingleOrderFragment.FINISHED -> {
                appComponent.netWork.deletePersonalTrainingOrder(id)
                        .doOnSubscribe { addDisposable(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            showTDialog()
                            page = 1
                            initData(page)
                        }
            }
            SingleOrderFragment.NEW_ORDERS -> {
                appComponent.netWork.takeOrder(id)
                        .doOnSubscribe { addDisposable(it) }
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            showTDialog()
                            page = 1
                            initData(page)
                        }
            }
        }
        showTDialog()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        adpter.clearAllDisposable()
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
