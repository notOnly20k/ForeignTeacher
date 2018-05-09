package com.xld.foreignteacher.ui.schedule

import android.support.design.widget.TabLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.TimeUtils
import com.google.gson.Gson
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherSchedule
import com.xld.foreignteacher.data.SetDiscount
import com.xld.foreignteacher.data.SetReservable
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.ext.doOnLoading
import com.xld.foreignteacher.ext.e
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.order.adapter.SingleOrderFragmentAdapter
import com.xld.foreignteacher.views.ScheduleDateTextView
import kotlinx.android.synthetic.main.activity_edit_schedule.*
import java.util.*

/**
 * Created by cz on 4/17/18.
 */
class EditScheduleActivity : BaseTranslateStatusActivity(), ScheduleCardFragment.ScheduleCardFragmentCallBack, TextWatcher {

    private val selectList = mutableListOf<Int>()
    override val contentViewResId: Int
        get() = R.layout.activity_edit_schedule
    override val changeTitleBar: Boolean
        get() = false


    private val fragmentList = ArrayList<BaseFragment>()
    private val titles = ArrayList<Date>()
    private var type = UNAVAILABLE
    private var updateUnavailableList: MutableList<TeacherSchedule>? = null
    private var updateDiscoutList: MutableList<SetDiscount>? = null
    private var editView: ScheduleDateTextView? = null
    private var id: Int? = null
    private var userId: Int? = null

    override fun initView() {
        initTitle()
        initScheduleView()
    }

    private fun initTitle() {
        type = intent.getStringExtra("type")
        title_bar.titlelayout.setBackgroundResource(R.color.color_black_1d1e24)
        title_bar.titleView.setTextColor(resources.getColor(R.color.yellow_ffcc00))
        title_bar.setLeftButton(R.mipmap.back_yellow, {
            finish()
        })
        when (type) {
            DISCOUNT -> {
                et_discount.addTextChangedListener(this)
                title_bar.setTitle(DISCOUNT)
                updateDiscoutList = mutableListOf()
                title_bar.addRightButton("Save") {
                    upData(type)
                }
                title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
            }
            UNAVAILABLE -> {
                title_bar.addRightButton("Save") {
                    upData(type)
                }
                title_bar.getRightButton(0).setTextColor(resources.getColor(R.color.yellow_ffcc00))
                updateUnavailableList = mutableListOf()
                title_bar.setTitle(UNAVAILABLE)
            }
            SCHEDULE -> {
                title_bar.setTitle(SCHEDULE)
                userId = intent.getIntExtra("id", -1)
            }
        }
    }

    private fun upData(type: String) {
        if (type == DISCOUNT) {
            if (editView != null && et_discount.text.isNotEmpty()) {
                updateDiscoutList!!.add(SetDiscount(id!!, et_discount.text.toString().toDouble() / 100))
            }
            logger.e { "$updateDiscoutList" }
            appComponent.netWork.setReservableAndDiscount(2, Gson().toJson(updateDiscoutList))
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe { finish() }
        } else if (type == UNAVAILABLE) {
            val list = mutableListOf<SetReservable>()
            updateUnavailableList!!.map {
                list.add(SetReservable(it.id, it.reservable))
            }
            appComponent.netWork.setReservableAndDiscount(1, Gson().toJson(list))
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .doOnLoading { isShowDialog(it) }
                    .subscribe { finish() }
        }
    }

    private fun showEditDiscount(visible: Boolean) {
        if (visible) {
            tv_discount_rate.visibility = View.VISIBLE
            et_discount.visibility = View.VISIBLE
        } else {
            tv_discount_rate.visibility = View.GONE
            et_discount.visibility = View.GONE
        }

    }

    private fun initScheduleView() {
        val c = Calendar.getInstance()
        val tag = when (type) {
            DISCOUNT -> ScheduleCardFragment.EDITDISCOUNT
            UNAVAILABLE -> ScheduleCardFragment.EDITDISABLE
            else -> ScheduleCardFragment.ENABLE
        }
        var id = appComponent.userHandler.getUser().id
        if (type == SCHEDULE) {
            id = userId!!
        }
        for (i in 0 until 15) {
            if (i == 0) {
                c.add(Calendar.DAY_OF_MONTH, 0)
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1)
            }

            fragmentList.add(ScheduleCardFragment.createInstance(TimeUtils.getTimeYMD(c.time), tag, id))
            titles.add(c.time)
        }

        vp_schedule.adapter = SingleOrderFragmentAdapter(supportFragmentManager, fragmentList)
        vp_schedule.offscreenPageLimit = 15
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



        vp_schedule.currentItem = 1
        tab_date.getTabAt(0)!!.select()

    }

    override fun scheduleClick(view: ScheduleDateTextView, checked: Boolean, position: Int, list: List<TeacherSchedule>) {
        when (type) {
            DISCOUNT -> {
                showEditDiscount(checked)
            }
            UNAVAILABLE -> {
                setUpdateList(list[position])
            }
            else -> {
            }
        }

    }

    private fun setUpdateList(teacherSchedule: TeacherSchedule) {
        if (updateUnavailableList!!.contains(teacherSchedule)) {
            updateUnavailableList!!.map {
                if (it.id == teacherSchedule.id) {
                    it.reservable = teacherSchedule.reservable
                }
            }
        } else {
            updateUnavailableList!!.add(teacherSchedule)
        }
    }


    override fun initData() {
        if (type == DISCOUNT) {
            appComponent.scheduleSubject
                    .doOnSubscribe { mCompositeDisposable.add(it) }
                    .subscribe { it ->

                        if (editView != null && et_discount.text.isNotEmpty()) {
                            editView!!.isShowDiscount(true)
                            editView!!.setDisCount(et_discount.text.toString())
                            if (id!=null) {
                                updateDiscoutList!!.add(SetDiscount(id!!, et_discount.text.toString().toDouble() / 100))
                            }
                            et_discount.setText("")
                        }
                        editView = it.second
                        id = it.first
                    }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }


    companion object {
        const val DISCOUNT = "Set DisCount"
        const val UNAVAILABLE = "Unavailable time"
        const val SCHEDULE = "Schedule"
    }
}
