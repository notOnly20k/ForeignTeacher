package com.whynuttalk.foreignteacher.ui

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.api.dto.*
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ext.e
import com.whynuttalk.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.whynuttalk.foreignteacher.ui.mine.commemt.CommentAdapter
import com.whynuttalk.foreignteacher.ui.mine.setting.BlockedListAdapter
import com.whynuttalk.foreignteacher.ui.mine.wallet.TransactionsAdapter
import com.whynuttalk.foreignteacher.ui.mine.wallet.WithDrawDetailAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.CurriculumAdapter
import com.whynuttalk.foreignteacher.ui.userinfo.adapter.FeedAdapter
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
    private var blockUserList = mutableListOf<BlockUser>()
    private var commentList = mutableListOf<TeacherDetail.CommentListBean>()
    private var offerList = mutableListOf<TeacherDetail.CurriculumListBean>()
    private var withDrawList = mutableListOf<WithDrawDetail>()
    private var feedList = mutableListOf<SquareListBean>()

    override fun initView() {
        rec_content.setLayoutManager(LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL })
        rec_content.setBackgroundColor(Color.WHITE)
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
                (adpter as BlockedListAdapter).setListener(object : BlockedListAdapter.OnItemClickListener {
                    override fun delet(id: Int, position: Int) {
                        appComponent.netWork.removeBlacklist(id)
                                .doOnSubscribe { mCompositeDisposable.add(it) }
                                .doOnLoading { isShowDialog(it) }
                                .subscribe {
                                    if (position >= 0 && position < blockUserList.size) {
                                        logger.e { "remove ===$position" }
                                        (adpter as BlockedListAdapter).removeData(position)
                                        adpter.notifyItemRemoved(position)

                                    }
                                }

                    }


                })
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
                        blockUserList.clear()
                        getBlacklist(page)
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
                        getBlacklist(page)
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
                getBlacklist(1)
            }
            OFFERS -> {
                getOffers(1)
            }
            FEEDS -> {
                getFeeds(1)
            }
        }
    }

    fun getBlacklist(page: Int) {
        appComponent.netWork
                .getBlacklist(appComponent.userHandler.getUser().id, page, 10)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { rec_content.isRefreshing = it }
                .subscribe { list ->
                    blockUserList.addAll(list)
                    (adpter as BlockedListAdapter).upDateList(blockUserList)
                    noMoreData(list)
                }
    }


    fun getTransactions(page: Int) {
        appComponent.netWork
                .getTeacherRecord(appComponent.userHandler.getUser().id, page, 10)
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
