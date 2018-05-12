package com.whynuttalk.foreignteacher.ui.square

import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import cn.sinata.xldutils.view.TitleBar
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.square.adapter.CommentDetailAdapter

/**
 * Created by cz on 5/2/18.
 */
class CommentDetailActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = false
    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView: SwipeRefreshRecyclerLayout
    @BindView(R.id.et_reply)
    lateinit var etReply: EditText

    lateinit var adapter: CommentDetailAdapter
    var id: Int = 0
    var squareId: Int = 0
    var page = 1

    override val contentViewResId: Int
        get() = R.layout.activity_all_reply

    override fun initView() {
        titleBar.setTitle("Comment Detail")
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.icon_back_black, View.OnClickListener { finish() })
        titleBar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(layoutManager)
        adapter = CommentDetailAdapter(this)
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
        etReply!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            val content = v.text.toString()
            if (TextUtils.isEmpty(content)) {
                showToast("Input Comment")
                return@OnEditorActionListener true
            }
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doComment()
            }
            false
        })
    }

    override fun initData() {
        val intent = getIntent()
        id = intent.getIntExtra("id", 2791)
        loadData()
    }

    private fun loadData() {
        appComponent.netWork.getSquareCommentList(id, page, 10)
                .doOnLoading { recyclerView.isRefreshing = it }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    squareId = it.squareComment!!.squareId
                    if (page == 1) {
                        adapter.setData(it)
                    } else {
                        adapter.addData(it.squareCommentLists!!)
                    }
                    recyclerView.isNoMoreData(it.squareCommentLists!!.isEmpty())
                }
    }

    private fun doComment() {
        appComponent.netWork.addSquareComment(appComponent.userHandler.getUser().id,squareId,etReply.text.toString(),id)
                .doOnLoading { isShowDialog(it) }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe {
                    etReply.setText("")
                    page=1
                    loadData()
                }
    }

}
