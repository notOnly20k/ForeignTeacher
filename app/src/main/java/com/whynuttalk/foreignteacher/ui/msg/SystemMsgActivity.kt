package com.whynuttalk.foreignteacher.ui.msg

import android.support.v7.widget.LinearLayoutManager
import butterknife.BindView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import cn.sinata.xldutils.view.TitleBar
import com.google.gson.Gson
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.SystemMessage
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.msg.adapter.SystemMsgAdapter

/**
 * Created by cz on 5/7/18.
 */
class SystemMsgActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_title_recycler
    override val changeTitleBar: Boolean
        get() = false
    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView: SwipeRefreshRecyclerLayout

    internal var page = 1
    internal var rows = 10
    lateinit var adapter: SystemMsgAdapter
    private var dataList = mutableListOf<SystemMessage>()

    override fun initView() {
        titleBar.setTitle("Official Notice")
        titleBar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.back_yellow, { finish() })
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(layoutManager)
        adapter = SystemMsgAdapter(this)
        recyclerView.setAdapter(adapter)
        recyclerView.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                loadData()
            }

            override fun onLoadMore() {
                page++
                loadData()
            }
        })
    }

    override fun initData() {
        loadData()
    }

    private fun loadData() {
        appComponent.netWork.getSystemMessage(appComponent.userHandler.getUser().id, 2, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { recyclerView?.isRefreshing = it }
                .subscribe {
                    if (page==1){
                        dataList.clear()
                                            val gson = Gson()
                    val systemMsg = gson.fromJson<SystemMessage>("{\n" +
                            "            \"id\": 1,\n" +
                            "            \"title\": \"测试\",\n" +
                            "            \"img\": \"http://img5.imgtn.bdimg.com/it/u=950004826,3164354653&fm=27&gp=0.jpg\",\n" +
                            "            \"url\": null,\n" +
                            "            \"addtime\": 1523159484000\n" +
                            "        }", SystemMessage::class.java)
                        dataList.add(systemMsg)
                        dataList.add(systemMsg)
                        dataList.add(systemMsg)
                    }
                    dataList.addAll(it)
                    adapter.setData(dataList)
                    if (it.isEmpty()) {
                        page--
                        recyclerView.isNoMoreData(true)
                    }
                }
//        val observer = object : DisposableObserver<List<SystemMsg>>() {
//            override fun onNext(systemMsgs: List<SystemMsg>) {
//                if (page == 1) {
//                    val gson = Gson()
//                    val systemMsg = gson.fromJson<SystemMsg>("{\n" +
//                            "            \"id\": 1,\n" +
//                            "            \"title\": \"测试\",\n" +
//                            "            \"img\": \"http://img5.imgtn.bdimg.com/it/u=950004826,3164354653&fm=27&gp=0.jpg\",\n" +
//                            "            \"url\": null,\n" +
//                            "            \"addtime\": 1523159484000\n" +
//                            "        }", SystemMsg::class.java)
//                    systemMsgs.add(systemMsg)
//                    systemMsgs.add(systemMsg)
//                    systemMsgs.add(systemMsg)
//                    recyclerView.isNoMoreData(false)
//                    adapter.setData(systemMsgs)
//                } else
//                    adapter.addData(systemMsgs)
//                page++
//                if (systemMsgs.size < rows)
//                    recyclerView.isNoMoreData(true)
//            }
//
//            override fun onError(e: Throwable) {
//                showToast(e.message)
//                dismissDialog()
//                recyclerView.isRefreshing = false
//            }
//
//            override fun onComplete() {
//                dismissDialog()
//                recyclerView.isRefreshing = false
//            }
//        }
//        mCompositeDisposable.add(observer)
//        MessagePresenter.getSystemMsg(SPUtils.getInt(Constants.USER_ID), 1, page, rows, observer)
    }
}
