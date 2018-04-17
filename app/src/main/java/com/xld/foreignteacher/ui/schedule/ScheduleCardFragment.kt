package com.xld.foreignteacher.ui.schedule

import android.os.Bundle
import cn.sinata.xldutils.fragment.BaseFragment
import cn.sinata.xldutils.utils.SPUtils
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherSchedule
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.views.ScheduleDateTextView
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/17/18.
 */
class ScheduleCardFragment : BaseFragment() {
    private val logger = LoggerFactory.getLogger("ScheduleCardFragment")
    override fun getContentViewLayoutID(): Int {
        return R.layout.item_schedule
    }
    private val dataList = mutableListOf<TeacherSchedule>()

    override fun onFirstVisibleToUser() {
        initDate()
    }

    private lateinit var scheduleCardFragmentCallBack: ScheduleCardFragmentCallBack

    fun initDate() {
        scheduleCardFragmentCallBack = activity as ScheduleCardFragmentCallBack
        val date = arguments!!.getString("date")
        val type = arguments!!.getInt("type")
        appComponent.netWork.getTeacherSchedule(SPUtils.getInt("id"), date)
                .subscribe { list ->
                    dataList.clear()
                    dataList.addAll(list)
                    initView(list)
                }
    }

    fun initView(list: List<TeacherSchedule>){
//        logger.e { "listSize ===${list.size}" +
//                "/n" +
//                "childCount===${gd_content.childCount}" }
//        list.map {  logger.e { "list ===${it}"} }
//        for (i in 0 until  gd_content.childCount) {
//            val view = (gd_content.getChildAt(i) as ScheduleDateTextView)
//            view.changByState(list[i].reservable)
//            view.setOnClickListener(object : ScheduleDateTextView.OnScheduleDateTextViewClickListner {
//                override fun click(isChecked: Boolean) {
//                    scheduleCardFragmentCallBack.scheduleClick(view, isChecked,list[i].id)
//                }
//
//            })
//            if (list[i].discount == 1.0){
//                view.isShowDiscount(false)
//            }else{
//                view.isShowDiscount(true)
//            }
//
//            view.setDisCount(list[i].discount.toString())
//        }
    }

    override fun onVisibleToUser() {
        if (dataList.isEmpty()) {
            initDate()
        }else{
            initView(dataList)
        }
    }

    override fun onInvisibleToUser() {

    }


    interface ScheduleCardFragmentCallBack {
        fun scheduleClick(view: ScheduleDateTextView, checked: Boolean, id: Int)
    }

    companion object {
        const val ENABLE = 1
        const val DISABLE = 2

        @JvmStatic
        fun createInstance(date: String, type: Int): ScheduleCardFragment {
            return ScheduleCardFragment().apply {
                arguments = Bundle(2).apply {
                    putString("date", date)
                    putInt("type", type)
                }

            }
        }
    }
}
