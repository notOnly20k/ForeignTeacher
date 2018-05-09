package com.xld.foreignteacher.ui.userinfo

import android.os.Bundle
import android.view.View
import cn.sinata.xldutils.activitys.BaseActivity
import cn.sinata.xldutils.utils.ActivityUtil
import com.hyphenate.easeui.EaseConstant
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ui.msg.ChatActivity
import com.xld.foreignteacher.ui.userinfo.adapter.StudentPageAdapter
import com.xld.foreignteacher.util.StatusBarUtil
import com.xld.foreignteacher.views.ViewPagerIndicator
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_student_main.*
import java.util.*

/**
 * Created by cz on 4/2/18.
 */
class StudentDetailActivity : BaseActivity() {
    private lateinit var mCompositeDisposable: CompositeDisposable
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
        StatusBarUtil.initBarHeight(this, null, fake_status_bar)
        initView()
        initData()
    }


    private fun initData() {
        appComponent.netWork.getUserInfo(id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog (it) }
                .subscribe {

                    val list = mutableListOf<String>()
                    if (it.albumList != null && it.albumList!!.isNotEmpty()) {
                        it.albumList!!.sortedBy { it.sort }.map { list.add(it.imgUrl!!) }
                        iv_user_head.setImageURI(list[0])
                        viewpager.adapter = StudentPageAdapter(this, list)
                        val viewPagerIndicator = ViewPagerIndicator(this, ll_indicator, R.mipmap.indicator_white, R.mipmap.indicator_yellow, list.size)
                        viewPagerIndicator.setupWithViewPager(viewpager)
                    }

                    tv_user_name.text = it.nickName
                    tv_user_name.setCompoundDrawablesWithIntrinsicBounds(null, null, if (it.sex === 1) resources.getDrawable(R.mipmap.icon_male) else resources.getDrawable(R.mipmap.icon_female), null)
                    tv_age.text = "${it.age} years old"

                }
        appComponent.netWork.getUserHomePage(id)
                .doOnSubscribe { mCompositeDisposable.add(it) }
                .doOnLoading { isShowDialog(it) }
                .subscribe {
                    //                    if (it.isLearnCard) {
                    val doubles = ArrayList<Double>() //按顺序传参：词汇 语法 听力 流利度 发音
                    doubles.add(it.vocabulary / 5.0 * 100)
                    doubles.add(it.grammar / 5.0 * 100)
                    doubles.add(it.listening / 5.0 * 100)
                    doubles.add(it.fluency / 5.0 * 100)
                    doubles.add(it.pronunciation / 5.0 * 100)
                    score_card.setDataList(doubles)
                    //成就
                    rl_achievement.visibility = View.VISIBLE
//                    }else{
//                        score_card.visibility = View.GONE;
//                        rl_achievement.visibility = View.VISIBLE;
//                    }
//                    if (it.isAchievement) {
                    tv_average_time.text = "${it.weekLongTime} hours"
                    tv_max_time.text = "${it.longTime} hours"
                    tv_learning_time.text = it.totalTime.toString()
                    tv_teachers_count.text = it.teacherNum.toString()
                    rl_achievement.visibility = View.VISIBLE
                    rl_achievement_invisible.visibility = View.GONE
//                    } else {
//                        rl_achievement.visibility = View.GONE
//                        rl_achievement_invisible.visibility = View.VISIBLE
//                    }
                }
//        val backDra=resources.getDrawable(R.mipmap.icon_woman)
//        backDra.setBounds( 0, 0,backDra.minimumHeight ,backDra.minimumHeight)
//        tv_user_name.setCompoundDrawables(null,null,backDra,null)
//        tv_user_name.compoundDrawablePadding=backDra.minimumHeight/2
    }

    private lateinit var adapter: StudentPageAdapter
    private var id = -1

    fun initView() {
        id = intent.getIntExtra("id", -1)
        mCompositeDisposable = CompositeDisposable()
        iv_back.setOnClickListener(View.OnClickListener { finish() })
        tv_chat.setOnClickListener { ActivityUtil.create(this).go(ChatActivity::class.java).put(EaseConstant.EXTRA_USER_ID, "waijiao_client_" + id).start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCompositeDisposable != null) { //取消订阅
            mCompositeDisposable.clear()
        }
    }
}
