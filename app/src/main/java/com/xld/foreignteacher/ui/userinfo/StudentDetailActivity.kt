package com.xld.foreignteacher.ui.userinfo

import android.os.Bundle
import android.view.View
import cn.sinata.xldutils.activitys.BaseActivity
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.xld.foreignteacher.util.StatusBarUtil
import com.xld.foreignteacher.views.ViewPagerIndicator
import kotlinx.android.synthetic.main.activity_student_main.*
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class StudentDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_main)
//        if (Build.VERSION.SDK_INT >= 21) {
//            val decorView = window.decorView
//            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            decorView.systemUiVisibility = option
//            window.statusBarColor = Color.TRANSPARENT
//        }
        StatusBarUtil.initStatus2(window)
        StatusBarUtil.initBarHeight(this,null,fake_status_bar)
        initView()
        initData()
    }


    private fun initData() {
        val backDra=resources.getDrawable(R.mipmap.icon_woman)
        backDra.setBounds( 0, 0,backDra.minimumHeight ,backDra.minimumHeight)
        tv_user_name.setCompoundDrawables(null,null,backDra,null)
        tv_user_name.compoundDrawablePadding=backDra.minimumHeight/2
        score_card.visibility = View.VISIBLE
        val doubles = ArrayList<Double>() //按顺序传参：词汇 语法 听力 流利度 发音
        doubles.add(20.5)
        doubles.add(40.5)
        doubles.add(60.5)
        doubles.add(80.5)
        doubles.add(90.5)
        score_card.setDataList(doubles)
        //成就
        rl_achievement.visibility = View.VISIBLE
    }

    private lateinit var adapter: StudentPageAdapter

    fun initView(){
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        val urls = ArrayList<String>()
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=7f62d8349c5414d66d647e1563fa0631&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F472309f790529822fb05a7e7ddca7bcb0a46d4e4.jpg")
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521814228383&di=c7a129ca9489f8b42379273069d4acf1&imgtype=0&src=http%3A%2F%2Fp.yjbys.com%2Fimage%2F20161206%2F1481008661538628.jpg")
        adapter = StudentPageAdapter(this, urls)
        viewpager.adapter = adapter
        val viewPagerIndicator = ViewPagerIndicator(this, ll_indicator, R.mipmap.indicator_white, R.mipmap.indicator_yellow, urls.size)
        viewPagerIndicator.setupWithViewPager(viewpager)
     }
}