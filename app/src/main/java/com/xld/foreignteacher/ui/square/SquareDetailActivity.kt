package com.xld.foreignteacher.ui.square

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import cn.sinata.xldutils.utils.DensityUtil
import cn.sinata.xldutils.utils.SPUtils
import cn.sinata.xldutils.view.TitleBar
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.report.ReportActivity
import com.xld.foreignteacher.ui.square.adapter.SquareDetailAdapter

/**
 * Created by cz on 4/1/18.
 */
class SquareDetailActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = false
    
    @BindView(R.id.title_bar)
    lateinit var titleBar: TitleBar
    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    private lateinit var adapter: SquareDetailAdapter
    private lateinit var loadMoreView: TextView
    private var squareId:Int = 0
    

    private var footerType = 0

    override val contentViewResId: Int
        get() = R.layout.activity_square_detail

    override fun initView() {
        squareId = intent.getIntExtra("id",0)
        titleBar.setTitle("Details")
        titleBar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        titleBar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        titleBar.setLeftButton(R.mipmap.back_yellow, { finish() })
        titleBar.addRightButton("report") {
            //todo 传id
             activityUtil.go(ReportActivity::class.java).put("id", squareId).put("type",ReportActivity.REPORT).start()
        }
        titleBar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = SquareDetailAdapter(this)
        loadMoreView = TextView(this)
        loadMoreView.setBackgroundColor(Color.WHITE)
        loadMoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        loadMoreView.setTextColor(resources.getColor(R.color.color_hint))
        loadMoreView.setPadding(0, DensityUtil.dip2px(this, 16f), 0, DensityUtil.dip2px(this, 16f))
        loadMoreView.gravity = Gravity.CENTER
        changeFooterView()
        recyclerView.adapter = adapter
    }

    override fun initData() {
        appComponent.netWork.getSquareDetail(squareId,SPUtils.getInt("id"),1,10)
                .doOnLoading { showProgress(it) }
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .subscribe { it->
                    adapter.setData(it)
                }

        footerType = TYPE_NOMORE
        changeFooterView()
    }

    /**
     * 根据数据状态改变footerview
     */
    private fun changeFooterView() {
        when (footerType) {
            TYPE_NODATA -> {
                loadMoreView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 48f))
                loadMoreView.text = "No more comment"
            }
            TYPE_LOADING -> {
                loadMoreView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                loadMoreView.text = "Loading..."
            }
            TYPE_NOMORE -> {
                loadMoreView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                loadMoreView.text = "No more comment"
            }
        }
        adapter.setFooterView(loadMoreView)
    }

    companion object {

        private val TYPE_NODATA = 0
        private val TYPE_NOMORE = 2
        private val TYPE_LOADING = 1
    }
}
