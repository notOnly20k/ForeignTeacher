package com.xld.foreignteacher.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.SquareListBean
import com.xld.foreignteacher.api.dto.TeacherDetail
import com.xld.foreignteacher.api.dto.TeacherRecord
import com.xld.foreignteacher.api.dto.WithDrawDetail
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.mine.commemt.CommentAdapter
import com.xld.foreignteacher.ui.mine.setting.BlockedListAdapter
import com.xld.foreignteacher.ui.mine.wallet.TransactionsAdapter
import com.xld.foreignteacher.ui.mine.wallet.WithDrawDetailAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.CurriculumAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.FeedAdapter
import kotlinx.android.synthetic.main.activity_list.*

/**
 * Created by cz on 4/13/18.
 */
class ListActivity : BaseTranslateStatusActivity() {
    override val contentViewResId: Int
        get() = R.layout.activity_list
    override val changeTitleBar: Boolean
        get() = false
    private lateinit var adpter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private var page = 1
    private var teacherRecordList = mutableListOf<TeacherRecord>()
    private var commentList = mutableListOf<TeacherDetail.CommentListBean>()
    private var offerList = mutableListOf<TeacherDetail.CurriculumListBean>()
    private var withDrawList = mutableListOf<WithDrawDetail>()
    private var feedList = mutableListOf<SquareListBean>()

    override fun initView() {
        rec_content.setLayoutManager(LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL })
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        when (intent.getStringExtra("type")) {
            WITHDRAWDETAIL -> {
                title_bar.setTitle(WITHDRAWDETAIL)
                adpter = WithDrawDetailAdapter(this)

            }
            TRANSACTIONS -> {
                title_bar.setTitle(TRANSACTIONS)
                adpter = TransactionsAdapter(this)
                (adpter as TransactionsAdapter).setListener(object : TransactionsAdapter.OnItemClickListener {
                    override fun loadMore() {
                        page++
                        getTransactions(page)
                    }

                })
            }

            COMMENT -> {
                title_bar.setTitle(COMMENT)

                adpter = CommentAdapter(this)

            }
            BLOCKED_LIST -> {
                title_bar.setTitle(BLOCKED_LIST)
                adpter = BlockedListAdapter(this)
            }

            OFFERS -> {
                title_bar.setTitle(OFFERS)
                adpter = CurriculumAdapter(this)
            }
            FEEDS -> {
                title_bar.setTitle(FEEDS)
                adpter = FeedAdapter(this)
            }

        }
        rec_content.setAdapter(adpter)
        rec_content.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                when (intent.getStringExtra("type")) {
                    WITHDRAWDETAIL -> {
                        withDrawList.clear()
                        getWithdrawDetails(page)
                    }
                    TRANSACTIONS -> {
                        teacherRecordList.clear()
                        getTransactions(page)
                    }

                    COMMENT -> {
                        commentList.clear()
                        getTeacherComments(page)
                    }
                    BLOCKED_LIST -> {

                    }

                    OFFERS -> {
                        offerList.clear()
                        getOffers(page)
                    }
                    FEEDS -> {
                        feedList.clear()
                        getFeeds(page)
                    }
                }
            }

            override fun onLoadMore() {
                page++
                when (intent.getStringExtra("type")) {
                    WITHDRAWDETAIL -> {
                        getWithdrawDetails(page)
                    }
                    TRANSACTIONS -> {
                        getTransactions(page)
                    }

                    COMMENT -> {
                        getTeacherComments(page)
                    }
                    BLOCKED_LIST -> {

                    }
                    OFFERS -> {
                        getOffers(page)
                    }
                    FEEDS -> {
                        getFeeds(page)
                    }
                }
            }

        })
    }

    override fun initData() {
        when (intent.getStringExtra("type")) {
            WITHDRAWDETAIL -> {
                getWithdrawDetails(1)
            }
            TRANSACTIONS -> {
                getTransactions(1)
            }

            COMMENT -> {
                getTeacherComments(1)
            }
            BLOCKED_LIST -> {

            }
            OFFERS -> {
                getOffers(1)
            }
            FEEDS -> {
                getFeeds(1)
            }
        }
    }


    fun getTransactions(page: Int) {
        appComponent.netWork
                .getTeacherRecord(appComponent.userHandler.getUser()!!.id, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content.isRefreshing = it }
                .subscribe { list ->
                    teacherRecordList.addAll(list)
                    (adpter as TransactionsAdapter).updateList(teacherRecordList)
                    noMoreData(list)
                }
    }

    fun getWithdrawDetails(page: Int) {
        appComponent.netWork
                .withdrawDetails(appComponent.userHandler.getUser()!!.id, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content?.isRefreshing = it }
                .subscribe { list ->
                    withDrawList.addAll(list)
                    (adpter as WithDrawDetailAdapter).updateList(withDrawList)
                    noMoreData(list)
                }
    }

    fun getOffers(page: Int) {
        val id = intent.getIntExtra("id", -1)
        appComponent.netWork
                .getCurriculumList(id, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content.isRefreshing = it }
                .subscribe { list ->
                    offerList.addAll(list)
                    (adpter as CurriculumAdapter).upDataList(offerList)
                    noMoreData(list)
                }
    }

    fun getFeeds(page: Int) {
        val id = intent.getIntExtra("id", -1)
        appComponent.netWork
                .getTeacherSquareList(id, page, 10, 2)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content.isRefreshing = it }
                .subscribe { list ->
                    feedList.addAll(list)
                    (adpter as FeedAdapter).upDataList(feedList)
                    noMoreData(list)
                }
    }

    fun getTeacherComments(page: Int) {
        val id = intent.getIntExtra("id", -1)
        appComponent.netWork.getTeacherCommentList(id, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content?.isRefreshing = it }
                .subscribe {
                    commentList.addAll(it)
                    (adpter as CommentAdapter).upDataList(commentList)
                    noMoreData(it)
                }
    }

    private fun noMoreData(list: List<Any>) {
        if (list.isEmpty()) {
            this.page--
        }
        rec_content?.isNoMoreData(list.isEmpty())
    }

    companion object {
        const val WITHDRAWDETAIL = "Withdraw detail"
        const val TRANSACTIONS = "Transactions"
        const val COMMENT = "Comment"
        const val BLOCKED_LIST = "Blocked list"
        const val OFFERS = "Offers"
        const val FEEDS = "Feeds"
    }
}
