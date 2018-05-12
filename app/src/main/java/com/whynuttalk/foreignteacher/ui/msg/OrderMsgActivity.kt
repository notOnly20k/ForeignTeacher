package com.whynuttalk.foreignteacher.ui.msg

import android.support.v7.widget.LinearLayoutManager
import butterknife.BindView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import cn.sinata.xldutils.view.TitleBar
import com.google.gson.Gson
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.OrderMessage
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.msg.adapter.OrderMsgAdapter

/**
 * Created by cz on 5/7/18.
 */
class OrderMsgActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_title_recycler
    override val changeTitleBar: Boolean
        get() = false
    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView: SwipeRefreshRecyclerLayout

    var page = 1
    var rows = 10
    var dataList = mutableListOf<OrderMessage>()
    lateinit var adapter: OrderMsgAdapter


    override fun initView() {
        titleBar.setTitle("Order Notice")
        titleBar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.back_yellow, { finish() })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(layoutManager)
        adapter = OrderMsgAdapter(this)
        recyclerView.setAdapter(adapter)
        recyclerView.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                loadData()
            }

            override fun onLoadMore() {
                loadData()
            }
        })
    }

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        appComponent.netWork.getOrderMessage(appComponent.userHandler.getUser().id, 2, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { recyclerView?.isRefreshing = it }
                .subscribe {
                    recyclerView!!.isNoMoreData(false)
                    if (page == 1) {
                        dataList.clear()
                        val gson = Gson()
                        val orderMsg = gson.fromJson<OrderMessage>(" {\n" +
                                "            \"id\": 1,\n" +
                                "            \"title\": \"测试\",\n" +
                                "            \"content\": \"测试测试测试测试\",\n" +
                                "            \"addtime\": 1523159514000,\n" +
                                "            \"userId\": 1,\n" +
                                "            \"type\": 1\n" +
                                "        }", OrderMessage::class.java)
                        dataList.add(orderMsg)
                        dataList.add(orderMsg)
                        dataList.add(orderMsg)
                    }
                    dataList.addAll(it)
                    adapter.setData(dataList)
                    recyclerView.isNoMoreData(it.isEmpty())
                }
//        val observer = object : DisposableObserver<List<OrderMsg>>() {
//            override fun onNext(systemMsgs: List<OrderMsg>) {
//                if (page == 1) {
//                    val gson = Gson()
//                    val orderMsg = gson.fromJson<OrderMsg>(" {\n" +
//                            "            \"id\": 1,\n" +
//                            "            \"title\": \"测试\",\n" +
//                            "            \"content\": \"测试测试测试测试\",\n" +
//                            "            \"addtime\": 1523159514000,\n" +
//                            "            \"userId\": 1,\n" +
//                            "            \"type\": 1\n" +
//                            "        }", OrderMsg::class.java)
//                    systemMsgs.add(orderMsg)
//                    systemMsgs.add(orderMsg)
//                    systemMsgs.add(orderMsg)
//                    recyclerView!!.isNoMoreData(false)
//                    adapter.setData(systemMsgs)
//                } else
//                    adapter.addData(systemMsgs)
//                page++
//                if (systemMsgs.size < rows)
//                    recyclerView!!.isNoMoreData(true)
//            }
//
//            override fun onError(e: Throwable) {
//                showToast(e.message)
//                dismissDialog()
//                recyclerView!!.isRefreshing = false
//            }
//
//            override fun onComplete() {
//                dismissDialog()
//                recyclerView!!.isRefreshing = false
//            }
//        }
//        mCompositeDisposable.add(observer)
//        MessagePresenter.getOrderMsg(SPUtils.getInt(Constants.USER_ID), 1, page, rows, observer)
    }
}
