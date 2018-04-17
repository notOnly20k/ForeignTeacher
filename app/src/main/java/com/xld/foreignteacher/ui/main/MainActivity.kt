package com.xld.foreignteacher.ui.main

import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import butterknife.BindView
import cn.sinata.xldutils.R.id.titleBar
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.SPUtils
import cn.sinata.xldutils.utils.Utils
import com.chaychan.library.BottomBarLayout
import com.timmy.tdialog.TDialog
import com.umeng.socialize.UMShareAPI
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.mine.MineFragment
import com.xld.foreignteacher.ui.msg.MessageFragment
import com.xld.foreignteacher.ui.order.OrderFragment
import com.xld.foreignteacher.ui.schedule.ScheduleCardFragment
import com.xld.foreignteacher.ui.schedule.ScheduleFragment
import com.xld.foreignteacher.ui.square.SquareFragment
import com.xld.foreignteacher.views.ScheduleDateTextView
import com.xld.foreignteacher.views.ViewPagerIndicator
import java.util.*


class MainActivity : BaseTranslateStatusActivity(),ScheduleCardFragment.ScheduleCardFragmentCallBack {
    override fun scheduleClick(view: ScheduleDateTextView, checked: Boolean, id: Int) {

    }

    override val changeTitleBar: Boolean
        get() = false

    override val contentViewResId: Int
        get() = R.layout.activity_main


    private val mFragmentList = ArrayList<BaseFragment>()

    @BindView(R.id.fl_content)
    lateinit var flContent: FrameLayout
    @BindView(R.id.bbl)
    lateinit var bbl: BottomBarLayout


    override fun initView() {
        titleBar
        mFragmentList.add(ScheduleFragment())
        mFragmentList.add(OrderFragment())
        mFragmentList.add(SquareFragment())
        mFragmentList.add(MessageFragment())
        mFragmentList.add(MineFragment())
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, mFragmentList[0]).commit()
        bbl.setOnItemSelectedListener { bottomBarItem, previousPosition, currentPosition -> changeFragment(previousPosition, currentPosition) }
        showFirstBanner()
    }

    override fun initData() {

    }

    private fun changeFragment(previousPosition: Int, currentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        if (currentPosition != previousPosition) {
            transaction.hide(mFragmentList[previousPosition])
            if (!mFragmentList[currentPosition].isAdded) {
                transaction.add(R.id.fl_content, mFragmentList[currentPosition])
            }
            transaction.show(mFragmentList[currentPosition]).commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 第一次打开应用展示
     */
    fun showFirstBanner() {
        val lastVersion = SPUtils.getInt("version_code")
        val currentVersion = Utils.getAppVersionCode(this)
        if (lastVersion == currentVersion)
            return
        val banners = ArrayList<Int>()
        val views = ArrayList<ImageView>()
        banners.add(R.mipmap.first_banner_1)
        banners.add(R.mipmap.first_banner_2)
        banners.add(R.mipmap.first_banner_3)
        banners.add(R.mipmap.first_banner_4)
        banners.add(R.mipmap.first_banner_5)
        for (i in banners.indices) {
            views.add(ImageView(this))
        }
        TDialog.Builder(supportFragmentManager)
                .setLayoutRes(R.layout.dialog_first_banner)
                .setScreenHeightAspect(this, 0.7f)
                .setScreenWidthAspect(this, 0.8f)
                .setDimAmount(0.8f)
                .setOnBindViewListener { viewHolder ->
                    //可对图片进行修改
                    val banner = viewHolder.getView<ViewPager>(R.id.banner)
                    banner.adapter = object : PagerAdapter() {
                        override fun getCount(): Int {
                            return banners.size
                        }

                        override fun isViewFromObject(view: View, `object`: Any): Boolean {
                            return view === `object`
                        }

                        override fun instantiateItem(container: ViewGroup, position: Int): Any {
                            views[position].setImageResource(banners[position])
                            container.addView(views[position])
                            return views[position]
                        }

                        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                            container.removeView(views[position])
                        }
                    }
                    //无限循环时使用
                    //                        banner.setImageLoader(new FrescoImageLoader())
                    //                                .setBannerStyle(BannerConfig.NOT_INDICATOR)
                    //                                .setImages(banners)
                    //                                .isAutoPlay(false)
                    //                                .start();
                    val indicators = viewHolder.getView<LinearLayout>(R.id.ll_indicator)
                    val indicator = ViewPagerIndicator(this@MainActivity, indicators, R.mipmap.indicator_white, R.mipmap.indicator_yellow, banners.size)
                    indicator.setupWithViewPager(banner)
                }
                .addOnClickListener(R.id.iv_close)
                .setOnViewClickListener { viewHolder, view, tDialog -> tDialog.dismiss() }
                .create()
                .show()
        SPUtils.save("version_code", currentVersion)
    }
}
