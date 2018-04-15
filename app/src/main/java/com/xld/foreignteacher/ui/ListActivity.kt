package com.xld.foreignteacher.ui

import android.support.v7.widget.LinearLayoutManager
import com.xld.foreignteacher.R
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

    override fun initView() {
        rec_content.setLayoutManager(LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL })
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, { finish() })
        when (intent.getStringExtra("type")) {
            WITHDRAWDETAIL->{
                title_bar.setTitle(WITHDRAWDETAIL)
                rec_content.setAdapter(WithDrawDetailAdapter(this))
            }
            TRANSACTIONS->{
                title_bar.setTitle(TRANSACTIONS)
                rec_content.setAdapter(TransactionsAdapter(this))
            }

            COMMENT->{
                title_bar.setTitle(COMMENT)
                rec_content.setAdapter(CommentAdapter(this, emptyList()))
            }
            BLOCKED_LIST->{
                title_bar.setTitle(BLOCKED_LIST)
                rec_content.setAdapter(BlockedListAdapter(this))
            }
        }
    }

    override fun initData() {

    }

    companion object {
        const val WITHDRAWDETAIL = "Withdraw detail"
        const val TRANSACTIONS = "Transactions"
        const val COMMENT = "Comment"
        const val BLOCKED_LIST = "Blocked list"
    }
}
