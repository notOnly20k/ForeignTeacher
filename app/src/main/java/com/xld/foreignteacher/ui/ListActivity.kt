package com.xld.foreignteacher.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cn.sinata.xldutils.utils.SPUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.mine.commemt.CommentAdapter
import com.xld.foreignteacher.ui.mine.setting.BlockedListAdapter
import com.xld.foreignteacher.ui.mine.wallet.TransactionsAdapter
import com.xld.foreignteacher.ui.mine.wallet.WithDrawDetailAdapter
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
                adpter = CommentAdapter(this, emptyList())
            }
            BLOCKED_LIST -> {
                title_bar.setTitle(BLOCKED_LIST)
                adpter = BlockedListAdapter(this)
            }

        }
        rec_content.setAdapter(adpter)
    }

    override fun initData() {
        when (intent.getStringExtra("type")) {
            WITHDRAWDETAIL -> {

            }
            TRANSACTIONS -> {
                getTransactions(1)
            }

            COMMENT -> {

            }
            BLOCKED_LIST -> {

            }
        }
    }

    fun getTransactions(page: Int) {
        appComponent.netWork
                .getTeacherRecord(SPUtils.getInt("id"), page, 1)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { showProgress(it) }
                .subscribe { list ->
                    (adpter as TransactionsAdapter).updateList(list)

                    rec_content.isNoMoreData(list.isEmpty())
                }
    }

    companion object {
        const val WITHDRAWDETAIL = "Withdraw detail"
        const val TRANSACTIONS = "Transactions"
        const val COMMENT = "Comment"
        const val BLOCKED_LIST = "Blocked list"
    }
}
