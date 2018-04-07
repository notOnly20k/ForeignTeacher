package com.xld.foreignteacher.ui.userinfo

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import cn.sinata.xldutils.activitys.BaseActivity
import cn.sinata.xldutils.utils.ActivityUtil
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.TeacherEvaluateAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.TeacherGoodsAdapter
import com.xld.foreignteacher.ui.userinfo.adapter.TeacherMomentAdapter
import com.xld.foreignteacher.util.StatusBarUtil
import com.xld.foreignteacher.views.ViewPagerIndicator
import kotlinx.android.synthetic.main.activity_teacher_detail.*

/**
 * Created by cz on 4/2/18.
 */
class TeacherDetailActivity: BaseActivity() {

    private lateinit var adapter: StudentPageAdapter
    private lateinit var activityUtil: ActivityUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_detail)
        ButterKnife.bind(this)
        StatusBarUtil.initStatus2(window)
        initView()
        initData()
    }

    private fun initView() {
        tv_self_introduce.setOnLongClickListener({
            //翻译后显示
            tv_translation.visibility = View.VISIBLE
            true
        })
        iv_back.setOnClickListener{finish()}
        activityUtil = ActivityUtil.create(this)
        val urls = ArrayList<String>()
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=7f62d8349c5414d66d647e1563fa0631&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F472309f790529822fb05a7e7ddca7bcb0a46d4e4.jpg")
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=c7a129ca9489f8b42379273069d4acf1&imgtype=0&src=http%3A%2F%2Fp.yjbys.com%2Fimage%2F20161206%2F1481008661538628.jpg")
        adapter = StudentPageAdapter(this, urls)
        viewpager.adapter = adapter
        val viewPagerIndicator = ViewPagerIndicator(this, ll_indicator, R.mipmap.indicator_white, R.mipmap.indicator_yellow, urls.size)
        viewPagerIndicator.setupWithViewPager(viewpager)
        val goods = ArrayList<String>()
        goods.add("")
        goods.add("")
        goods.add("")
        lv_goods.adapter = TeacherGoodsAdapter(this, goods)
        lv_moment.adapter = TeacherMomentAdapter(this, goods)
        lv_evaluate.adapter = TeacherEvaluateAdapter(this, goods)
    }

    private fun initData() {

    }
}