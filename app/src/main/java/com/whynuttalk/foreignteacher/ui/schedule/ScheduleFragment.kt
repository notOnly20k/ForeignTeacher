package com.whynuttalk.foreignteacher.ui.schedule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.ActivityUtil
import cn.sinata.xldutils.utils.TimeUtils
import com.timmy.tdialog.TDialog
import com.whynuttalk.foreignteacher.R
import com.whynuttalk.foreignteacher.ext.appComponent
import com.whynuttalk.foreignteacher.ext.doOnLoading
import com.whynuttalk.foreignteacher.ui.dialog.CustomDialog
import com.whynuttalk.foreignteacher.ui.dialog.CustomPopWindow
import com.whynuttalk.foreignteacher.ui.order.adapter.SingleOrderFragmentAdapter
import com.whynuttalk.foreignteacher.ui.report.ReportActivity
import com.whynuttalk.foreignteacher.ui.schedule.adapter.MyGroupOfferAdapter
import com.whynuttalk.foreignteacher.ui.schedule.adapter.MyOfferAdapter
import kotlinx.android.synthetic.main.fragment_schedule.*
import org.slf4j.LoggerFactory
import java.util.*


/**
 * Created by cz on 3/29/18.
 */
class ScheduleFragment : BaseFragment() {
    lateinit var activityUtil: ActivityUtil
    override fun getContentViewLayoutID(): Int {
        return R.layout.fragment_schedule
    }

    private val logger = LoggerFactory.getLogger("ScheduleFragment")
    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<Date>()
    var page2 = 1
    var page1 = 1


    private lateinit var myOfferAdapter: MyOfferAdapter
    private lateinit var myGroupOfferAdapter: MyGroupOfferAdapter
    private lateinit var popWindow: CustomPopWindow

    override fun onFirstVisibleToUser() {
        activityUtil = ActivityUtil.create(activity)
        disableAutoScrollToBottom()
        initScheduleView()
        initOfferView()
        tv_title_right.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.item_pop_schedule, null, false)
            view.findViewById<TextView>(R.id.tv_offer).setOnClickListener {
                startActivity(Intent(activity, AddOfferActivity::class.java).putExtra("type", AddOfferActivity.ADD))
                popWindow.dissmiss()
            }
            view.findViewById<TextView>(R.id.tv_group).setOnClickListener {
                startActivity(Intent(activity, AddGroupOfferActivity::class.java))
                popWindow.dissmiss()
            }
            popWindow = CustomPopWindow.PopupWindowBuilder(context)
                    .setView(view)
                    .setFocusable(true)
                    .enableBackgroundDark(true)
                    .setBgDarkAlpha(40F)
                    .setOutsideTouchable(true)
                    .create()
            popWindow.showAsDropDown(tv_title_right, 0, 10)

        }
        tv_set_unavailable_time.setOnClickListener {
            activityUtil.go(EditScheduleActivity::class.java).put("type", EditScheduleActivity.UNAVAILABLE).start()
        }
        tv_set_discount.setOnClickListener {
            activityUtil.go(EditScheduleActivity::class.java).put("type", EditScheduleActivity.DISCOUNT).start()
        }

        mSwipeRefreshLayout.setOnRefreshListener {
            initGroupData()
            initSingleData()
        }
        initGroupData()
        initSingleData()

    }

    private fun initOfferView() {
        val manager = LinearLayoutManager(context).apply { orientation = LinearLayout.VERTICAL }
        rec_content.layoutManager = manager
        rec_content.isNestedScrollingEnabled = false
        myGroupOfferAdapter = MyGroupOfferAdapter(context)
        myGroupOfferAdapter.setCallback(object : MyGroupOfferAdapter.OnItemClickCallback {

            override fun onCancelItem(id: Int) {
                activityUtil.go(ReportActivity::class.java).put("type", ReportActivity.CANCEL_REQUEST)
                        .put("id", id).startForResult(1)
            }

            override fun onDeletItem(id: Int) {
                CustomDialog.Builder()
                        .create()
                        .setTitle("Are you sure you want to delete this record?")
                        .setButton1Text("Yes")
                        .setButton2Text("No")
                        .setDialogListene(object : CustomDialog.CustomDialogListener {
                            override fun clickButton1(customDialog: CustomDialog) {

                                appComponent.netWork.delFigh(id)
                                        .doOnSubscribe { addDisposable(it) }
                                        .doOnLoading { isShowDialog(it) }
                                        .subscribe {
                                            customDialog.dismiss()
                                            initGroupData()
                                            showTDialog()
                                        }
                            }

                            override fun clickButton2(customDialog: CustomDialog) {
                                customDialog.dismiss()
                            }

                        }).show(fragmentManager, "delete_order")
            }

        })
        myOfferAdapter = MyOfferAdapter(context)
        (rb_my_offer as RadioButton).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                img_line_right.visibility = View.VISIBLE
                img_line_left.visibility = View.VISIBLE
                rec_content.adapter = myOfferAdapter
            }
        }

        (rb_my_group_offer as RadioButton).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                img_line_right.visibility = View.GONE
                img_line_left.visibility = View.GONE
                rec_content.adapter = myGroupOfferAdapter
            }
        }
        (rb_my_offer as RadioButton).isChecked = true
    }

    fun showTDialog() {
        val dialog = TDialog.Builder(fragmentManager)
                .setLayoutRes(R.layout.dialog_inform_ok)    //设置弹窗展示的xml布局
                .setScreenWidthAspect(activity, 0.4f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                .setScreenHeightAspect(activity, 0.2f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                .setGravity(Gravity.CENTER)     //设置弹窗展示位置
                .setTag("SubscribeDialog")   //设置Tag
                .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                .setCancelable(true)    //弹窗是否可以取消
                .create()   //创建TDialog
                .show()    //展示
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            initGroupData()
        }
    }

    override fun onVisibleToUser() {

    }

    override fun onInvisibleToUser() {

    }

    private fun initScheduleView() {
        val c = Calendar.getInstance()
        val id = appComponent.userHandler.getUser().id
        for (i in 0 until 15) {
            if (i == 0) {
                c.add(Calendar.DAY_OF_MONTH, 0)
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1)
            }
            fragmentList.add(ScheduleCardFragment.createInstance(TimeUtils.getTimeYMD(c.time), ScheduleCardFragment.ENABLE, id))
            titles.add(c.time)
        }

        vp_schedule.adapter = SingleOrderFragmentAdapter(childFragmentManager, fragmentList)
        tab_date.setupWithViewPager(vp_schedule)
        tab_date.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tvDate = tab.customView?.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView?.findViewById<TextView>(R.id.tv_week)
                tvDate?.isSelected = false
                tvWeek?.isSelected = false
                tvDate?.textSize = 15F
                tvWeek?.textSize = 14F

            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                val tvDate = tab.customView!!.findViewById<TextView>(R.id.tv_datetime)
                val tvWeek = tab.customView!!.findViewById<TextView>(R.id.tv_week)
                tvDate.isSelected = true
                tvWeek.isSelected = true
                tvDate.textSize = 16F
                tvWeek.textSize = 15F

                vp_schedule.currentItem = tab.position

            }

        })
        for (i in 0 until titles.size) {
            val tab = tab_date.getTabAt(i)
            tab!!.setCustomView(R.layout.item_tab_date)
            tab.customView!!.findViewById<TextView>(R.id.tv_datetime).text = TimeUtils.getTimeDM(titles[i])
            tab.customView!!.findViewById<TextView>(R.id.tv_week).text = TimeUtils.getWeek(titles[i])
        }



        vp_schedule.currentItem = 0
        tab_date.getTabAt(0)!!.select()

    }

    fun initGroupData() {
        appComponent.netWork.getTeacherFightFirstList(appComponent.userHandler.getUser().id, 10, page2)
                .doOnSubscribe { addDisposable(it) }
                .doOnLoading {
                    isShowDialog(it)
                    mSwipeRefreshLayout?.isRefreshing = it
                }
                .subscribe {
                    myGroupOfferAdapter.setData(it)
                }
    }

    fun initSingleData() {
        appComponent.netWork.getTeacherCurricuumFirstList(appComponent.userHandler.getUser().id, page1, 10)
                .doOnSubscribe { addDisposable(it) }
                .doOnLoading {
                    mSwipeRefreshLayout?.isRefreshing = it
                    isShowDialog(it)
                }
                .subscribe {
                    myOfferAdapter.setData(it)
                }
    }

    private fun disableAutoScrollToBottom() {
        scroll.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
        scroll.isFocusable = true
        scroll.isFocusableInTouchMode = true
        scroll.setOnTouchListener { v, _ ->
            v.requestFocusFromTouch()
            false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        userVisibleHint = true
        return rootView
    }

}
