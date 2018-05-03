package com.xld.foreignteacher.ui.square

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.view.SwipeRefreshRecyclerLayout
import com.timmy.tdialog.TDialog
import com.timmy.tdialog.listener.OnViewClickListener
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.SquareDate
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.square.SquareCityActivity.Companion.CURRENT_CITY
import com.xld.foreignteacher.ui.square.SquareCityActivity.Companion.SELECTCITY
import com.xld.foreignteacher.ui.square.adapter.SquareAdapter
import kotlinx.android.synthetic.main.fragment_square.*
import org.slf4j.LoggerFactory

/**
 * Created by cz on 3/29/18.
 */
class SquareFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("SquareFragment")
    private var page = 1
    private var dataList = mutableListOf<SquareDate>()
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_square
    }

    private lateinit var adapter: SquareAdapter
    private lateinit var activityUtil: ActivityUtil

    override fun onFirstVisibleToUser() {
        activityUtil = ActivityUtil.create(activity)
        adapter = SquareAdapter(activity!!, fragmentManager!!)
        adapter.setClickCallback(object : SquareAdapter.ItemClickCallback {
            override fun doLike(squareId: Int) {
                appComponent.netWork
                        .addGiveThum(squareId, context.appComponent.userHandler.getUser()!!.id)
                        .doOnLoading {}
                        .subscribe {
                            page = 1
                            initDate()
                        }
            }

            override fun onReply(data: MutableList<SquareDate>, position: Int, id: Int) {
                evaluateDialog(data, position, id)
            }

            override fun onComment(data: List<SquareDate>, position: Int) {
                evaluateDialog(data, position, null)
            }

        })
        rec_square.setAdapter(adapter)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rec_square.setLayoutManager(layoutManager)

        tv_choose_city.setOnClickListener {
            activityUtil.go(SquareCityActivity::class.java).put(CURRENT_CITY, "北京市市辖区").startForResult(SELECTCITY)
        }

        tv_edit.setOnClickListener {
            activityUtil.go(MomentActivity::class.java).start()
        }

        rec_square.setOnRefreshListener(object : SwipeRefreshRecyclerLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                initDate()
            }

            override fun onLoadMore() {
                page++
                initDate()
            }

        })

        initDate()
    }

    private fun initDate() {
        appComponent.netWork
                .getSquareList(appComponent.userHandler.getUser()!!.id, page, 10)
                .doOnLoading {
                    if (rec_square != null)
                        rec_square.isRefreshing = it
                }
                .subscribe { list ->
                    if (page == 1) {
                        dataList.clear()
                        dataList.addAll(list)
                    } else {
                        dataList.addAll(list)
                    }
                    adapter.upDateList(dataList)
                    if (list != null && list.isEmpty()) {
                        this.page--
                    }
                    rec_square?.isNoMoreData(list.isEmpty())
                }
    }

    /**
     * 评论
     */
    fun evaluateDialog(data: List<SquareDate>, position: Int, commentId: Int?) {
        TDialog.Builder(fragmentManager)
                .setLayoutRes(R.layout.dialog_evaluate)
                .setScreenWidthAspect(activity, 1.0f)
                .setGravity(Gravity.BOTTOM)
                .addOnClickListener(R.id.btn_evluate)
                .setOnDismissListener {
                    //隐藏键盘
                }
                .setOnBindViewListener { viewHolder ->
                    val editText = viewHolder.getView<EditText>(R.id.editText)
                    editText.post(Runnable {
                        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                    appComponent.netWork.addSquareComment(appComponent.userHandler.getUser().id, data[position].id, content, commentId)
                            .subscribe {
                                page = 1
                                initDate()
                                tDialog.dismiss()
                            }

                })
                .create()
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        logger.e { data!!.getStringExtra("city") }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO: inflate a fragment view
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

    override fun onVisibleToUser() {
    }

    override fun onInvisibleToUser() {
    }

}
