package com.xld.foreignteacher.ui.square

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import cn.sinata.xldutils.view.TitleBar
import com.timmy.tdialog.TDialog
import com.timmy.tdialog.listener.OnViewClickListener
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.report.ReportActivity
import com.xld.foreignteacher.ui.square.adapter.SquareDetailAdapter
import kotlinx.android.synthetic.main.activity_square_detail.*

/**
 * Created by cz on 4/1/18.
 */
class SquareDetailActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = false

    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView: SwipeRefreshRecyclerLayout

    private lateinit var adapter: SquareDetailAdapter
    private lateinit var loadMoreView: TextView
    private var squareId: Int = 0
    var page = 1
    var id = -1


    override val contentViewResId: Int
        get() = R.layout.activity_square_detail

    override fun initView() {
        squareId = intent.getIntExtra("id", 0)
        titleBar.setTitle("Details")
        titleBar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.back_yellow, { finish() })
        titleBar.addRightButton("report") {
            //todo 传id
            activityUtil.go(ReportActivity::class.java).put("id", squareId).put("type", ReportActivity.REPORT).start()
        }
        titleBar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.setLayoutManager(layoutManager)
        adapter = SquareDetailAdapter(this)
        adapter.setOnClickCallback(object : SquareDetailAdapter.OnClickCallback {
            override fun doLike(squareId: Int) {
                appComponent.netWork
                        .addGiveThum(squareId, appComponent.userHandler.getUser()!!.id)
                        .doOnLoading { isShowDialog(it) }
                        .subscribe {
                            page = 1
                            loadData()
                        }
            }

            override fun onComment() {
                evaluateDialog(null)
            }

            override fun onReply(commentId: Int) {
                evaluateDialog(commentId)
            }

        })
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

        img_like.setOnClickListener {
            appComponent.netWork
                    .addGiveThum(id, appComponent.userHandler.getUser()!!.id)
                    .doOnLoading { isShowDialog(it) }
                    .subscribe {
                        page = 1
                        loadData()
                    }
        }
        img_comment.setOnClickListener {
            evaluateDialog(null)
        }
    }

    override fun initData() {
        val intent = intent
        id = intent.getIntExtra("id", 0)
        loadData()
    }


    /**
     * 评论
     */
    fun evaluateDialog(commentId: Int?) {
        TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_evaluate)
                .setScreenWidthAspect(this, 1.0f)
                .setGravity(Gravity.BOTTOM)
                .addOnClickListener(R.id.btn_evluate)
                .setOnDismissListener {
                    //隐藏键盘
                }
                .setOnBindViewListener { viewHolder ->
                    val editText = viewHolder.getView<EditText>(R.id.editText)
                    editText.post(Runnable {
                        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.showSoftInput(editText, 0)
                    })
                }
                .setOnViewClickListener(OnViewClickListener { viewHolder, view, tDialog ->
                    val editText = viewHolder.getView<EditText>(R.id.editText)
                    val content = editText.text.toString()
                    if (TextUtils.isEmpty(content)) {
                        showToast("Input Comment")
                        return@OnViewClickListener
                    }
                    appComponent.netWork.addSquareComment(appComponent.userHandler.getUser().id, id, content, commentId)
                            .subscribe {
                                page = 1
                                loadData()
                                tDialog.dismiss()
                            }

                })
                .create()
                .show()
    }

    fun loadData() {
        appComponent.netWork.getSquareDetail(squareId, appComponent.userHandler.getUser()!!.id, page, 10)
                .doOnLoading { recyclerView.isRefreshing = it }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe { it ->
                    if (page == 1) {
                        adapter.setData(it)
                    } else {
                        adapter.addData(it.squareCommentList!!)
                    }
                    recyclerView.isNoMoreData(it.squareCommentList == null || it.squareCommentList!!.size < 10 || it.squareCommentList!!.isEmpty())
                }
    }

}
