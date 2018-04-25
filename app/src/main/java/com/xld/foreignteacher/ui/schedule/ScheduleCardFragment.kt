package com.xld.foreignteacher.ui.schedule

import android.os.Bundle
import cn.sinata.xldutils.fragment.BaseFragment
import com.xld.foreignteacher.R
import com.xld.foreignteacher.api.dto.TeacherSchedule
import com.xld.foreignteacher.ext.appComponent
import com.xld.foreignteacher.views.ScheduleDateTextView
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.DISABLE
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.DISABLECENTER
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.DISABLELEFT
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.DISABLERIGHT
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.EDIT
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.NORMAL
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.OPEN
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.OPENCENTER
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.OPENLEFT
import com.xld.foreignteacher.views.ScheduleDateTextView.Companion.OPENRIGHT
import kotlinx.android.synthetic.main.item_schedule.*
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
    private var editView: ScheduleDateTextView? = null

    override fun onFirstVisibleToUser() {
        initDate()
    }

    override fun onVisibleToUser() {
        if (type == ENABLE) {
            initDate()
        }

    }

    var type: Int = ENABLE

    private lateinit var scheduleCardFragmentCallBack: ScheduleCardFragmentCallBack

    fun initDate() {
        scheduleCardFragmentCallBack = activity as ScheduleCardFragmentCallBack
        val date = arguments!!.getString("date")
        type = arguments!!.getInt("type")
        val id =arguments!!.getString("id")
        appComponent.netWork.getTeacherSchedule(1, date)
                .doOnSubscribe { addDisposable(it) }
                .subscribe { list ->
                    dataList.clear()
                    dataList.addAll(list)
                    if (isVisible) {
                        initView(list)
                    }
                }
    }

    fun initView(list: List<TeacherSchedule>) {
        for (i in 0 until list.size) {
            if (isVisible) {
                val view = (gd_content.getChildAt(i) as ScheduleDateTextView)
                changeViewStateWithOthers(view, i, list[i].reservable)
                if (type == EDITDISCOUNT && list[i].reservable == OPEN) {
                    changeViewStateWithOthers(view, i, DISABLE)
                }
                view.setOnClickListener(object : ScheduleDateTextView.OnScheduleDateTextViewClickListner {
                    override fun click(isChecked: Boolean) {
                        if (type == EDITDISABLE) {
                            if (list[i].reservable != OPEN) {
                                if (list[i].reservable == DISABLE) {
                                    changeViewStateWithOthers(view, i, NORMAL)
                                } else {
                                    changeViewStateWithOthers(view, i, DISABLE)
                                }
                                list[i].reservable = view.getClassState()
                                scheduleCardFragmentCallBack.scheduleClick(view, isChecked, i, list)
                            }
                        } else if (type == EDITDISCOUNT) {
                            if (list[i].reservable == NORMAL) {
                                scheduleCardFragmentCallBack.scheduleClick(view, isChecked, i, list)
                                appComponent.scheduleSubject.onNext(Pair(list[i].id, view))
                                editView = view
                                if (isChecked) {
                                    view.changByState(EDIT)
                                } else {
                                    changeViewStateWithOthers(view, i, NORMAL)
                                }
                            }
                        }
                    }

                })
                if (list[i].discount == 1.0) {
                    view.isShowDiscount(false)
                } else {
                    view.setDisCount((list[i].discount * 100).toInt().toString())
                    view.isShowDiscount(true)
                }
            }
        }

        appComponent.scheduleSubject
                .doOnSubscribe { addDisposable(it) }
                .subscribe {
                    if (editView != null && it.second != editView) {
                        editView!!.changByState(1)
                        editView!!.setButtonCheckState(false)
                    } else if (editView != null && it.second == editView) {
                        editView!!.changByState(EDIT)
                    }
                }
    }


    private fun changeViewStateWithOthers(view: ScheduleDateTextView, position: Int, state: Int) {

        when (view.tag) {
            "right" -> {
                val firstView = gd_content.getChildAt(position - 2) as ScheduleDateTextView
                val secondView = gd_content.getChildAt(position - 1) as ScheduleDateTextView
                when {
                    secondView.getClassState() == OPEN && firstView.getClassState() == OPEN -> {
                        when (state) {
                            OPEN -> {
                                secondView.changByState(OPENCENTER)
                                view.changByState(OPENCENTER)
                            }
                            DISABLE -> {
                                secondView.changByState(OPENLEFT)
                                view.changByState(DISABLERIGHT)
                            }
                            else -> {
                                secondView.changByState(OPENLEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    secondView.getClassState() == OPEN && firstView.getClassState() == DISABLE -> {
                        when (state) {
                            OPEN -> {
                                secondView.changByState(OPENRIGHT)
                                view.changByState(OPENCENTER)
                            }
                            DISABLE -> {
                                secondView.changByState(OPEN)
                                view.changByState(DISABLE)
                            }
                            else -> {
                                secondView.changByState(OPEN)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    secondView.getClassState() == OPEN && firstView.getClassState() == NORMAL -> {
                        when (state) {
                            OPEN -> {
                                secondView.changByState(OPENRIGHT)
                                view.changByState(OPENCENTER)
                            }
                            DISABLE -> {
                                secondView.changByState(OPEN)
                                view.changByState(DISABLERIGHT)
                            }
                            else -> {
                                secondView.changByState(OPEN)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    secondView.getClassState() == DISABLE && firstView.getClassState() == OPEN -> {
                        when (state) {
                            OPEN -> {
                                secondView.changByState(DISABLE)
                                view.changByState(OPENRIGHT)
                            }
                            DISABLE -> {
                                secondView.changByState(DISABLERIGHT)
                                view.changByState(DISABLECENTER)
                            }
                            else -> {
                                secondView.changByState(DISABLE)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    secondView.getClassState() == DISABLE && firstView.getClassState() == DISABLE -> {

                        when (state) {
                            OPEN -> {
                                secondView.changByState(DISABLELEFT)
                                view.changByState(OPENRIGHT)
                            }
                            DISABLE -> {
                                secondView.changByState(DISABLECENTER)
                                view.changByState(DISABLECENTER)
                            }
                            else -> {
                                secondView.changByState(DISABLELEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    secondView.getClassState() == DISABLE && firstView.getClassState() == NORMAL -> {
                        when (state) {
                            OPEN -> {
                                secondView.changByState(DISABLE)
                                view.changByState(OPENRIGHT)
                            }
                            DISABLE -> {
                                secondView.changByState(DISABLERIGHT)
                                view.changByState(DISABLECENTER)
                            }
                            else -> {
                                secondView.changByState(DISABLE)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    else -> {
                        when (state) {
                            OPEN -> view.changByState(OPENRIGHT)
                            DISABLE -> view.changByState(DISABLERIGHT)
                            else -> view.changByState(NORMAL)
                        }
                    }
                }
            }
            "center" -> {
                val firstView = gd_content.getChildAt(position - 1) as ScheduleDateTextView
                val thirdView = gd_content.getChildAt(position + 1) as ScheduleDateTextView
                when {
                    firstView.getClassState() == OPEN && thirdView.getClassState() == OPEN -> {
                        when (state) {
                            OPEN -> {
                                firstView.changByState(OPENCENTER)
                                thirdView.changByState(OPENCENTER)
                                view.changByState(OPENCENTER)
                            }
                            DISABLE -> {
                                firstView.changByState(OPENLEFT)
                                thirdView.changByState(OPENRIGHT)
                                view.changByState(DISABLE)
                            }
                            else -> {
                                firstView.changByState(OPENLEFT)
                                thirdView.changByState(OPENRIGHT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == OPEN && thirdView.getClassState() == DISABLE -> {

                        when (state) {
                            OPEN -> {
                                firstView.changByState(OPENCENTER)
                                thirdView.changByState(DISABLERIGHT)
                                view.changByState(OPENLEFT)
                            }
                            DISABLE -> {
                                thirdView.changByState(DISABLECENTER)
                                firstView.changByState(OPENLEFT)
                                view.changByState(DISABLERIGHT)
                            }
                            else -> {
                                thirdView.changByState(DISABLERIGHT)
                                firstView.changByState(OPENLEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == DISABLE && thirdView.getClassState() == OPEN -> {

                        when (state) {
                            OPEN -> {
                                firstView.changByState(DISABLELEFT)
                                thirdView.changByState(OPENCENTER)
                                view.changByState(OPENRIGHT)
                            }
                            DISABLE -> {
                                thirdView.changByState(OPENRIGHT)
                                firstView.changByState(DISABLECENTER)
                                view.changByState(DISABLELEFT)
                            }
                            else -> {
                                thirdView.changByState(OPENRIGHT)
                                firstView.changByState(DISABLELEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == DISABLE && thirdView.getClassState() == DISABLE -> {

                        when (state) {
                            OPEN -> {
                                firstView.changByState(DISABLELEFT)
                                thirdView.changByState(DISABLERIGHT)
                                view.changByState(OPEN)
                            }
                            DISABLE -> {
                                thirdView.changByState(DISABLECENTER)
                                firstView.changByState(DISABLECENTER)
                                view.changByState(DISABLECENTER)
                            }
                            else -> {
                                thirdView.changByState(DISABLERIGHT)
                                firstView.changByState(DISABLELEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == NORMAL && thirdView.getClassState() == OPEN -> {
                        when (state) {
                            OPEN -> {
                                thirdView.changByState(OPENCENTER)
                                view.changByState(OPENRIGHT)
                            }
                            DISABLE -> {
                                thirdView.changByState(OPENRIGHT)
                                view.changByState(DISABLE)
                            }
                            else -> {
                                thirdView.changByState(OPENRIGHT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == NORMAL && thirdView.getClassState() == DISABLE -> {
                        when (state) {
                            OPEN -> {
                                thirdView.changByState(DISABLERIGHT)
                                view.changByState(OPEN)
                            }
                            DISABLE -> {
                                thirdView.changByState(DISABLECENTER)
                                view.changByState(DISABLERIGHT)
                            }
                            else -> {
                                thirdView.changByState(DISABLERIGHT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == OPEN && thirdView.getClassState() == NORMAL -> {
                        when (state) {
                            OPEN -> {
                                firstView.changByState(OPENCENTER)
                                view.changByState(OPENLEFT)
                            }
                            DISABLE -> {
                                firstView.changByState(OPENLEFT)
                                view.changByState(DISABLE)
                            }
                            else -> {
                                firstView.changByState(OPENLEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    firstView.getClassState() == DISABLE && thirdView.getClassState() == NORMAL -> {
                        when (state) {
                            OPEN -> {
                                firstView.changByState(DISABLELEFT)
                                view.changByState(OPEN)
                            }
                            DISABLE -> {
                                firstView.changByState(DISABLECENTER)
                                view.changByState(DISABLELEFT)
                            }
                            else -> {
                                firstView.changByState(DISABLELEFT)
                                view.changByState(NORMAL)
                            }
                        }
                    }
                    else -> {
                        when (state) {
                            OPEN -> view.changByState(OPEN)
                            DISABLE -> view.changByState(DISABLE)
                            else -> view.changByState(NORMAL)
                        }
                    }
                }
            }
            "left" -> {
                val secondView = gd_content.getChildAt(position + 1) as ScheduleDateTextView
                val thirdView = gd_content.getChildAt(position + 2) as ScheduleDateTextView
                when {
                    secondView.getClassState() == OPEN -> {
                        when {
                            thirdView.getClassState() == OPEN -> when (state) {
                                OPEN -> {
                                    secondView.changByState(OPENCENTER)
                                    view.changByState(OPENCENTER)
                                }
                                DISABLE -> {
                                    secondView.changByState(OPENRIGHT)
                                    view.changByState(DISABLELEFT)
                                }
                                else -> {
                                    secondView.changByState(OPENRIGHT)
                                    view.changByState(NORMAL)
                                }
                            }
                            thirdView.getClassState() == DISABLE -> when (state) {
                                OPEN -> {
                                    secondView.changByState(OPENLEFT)
                                    view.changByState(OPENCENTER)
                                }
                                DISABLE -> {
                                    secondView.changByState(OPEN)
                                    view.changByState(DISABLELEFT)
                                }
                                else -> {
                                    secondView.changByState(OPEN)
                                    view.changByState(NORMAL)
                                }
                            }
                            else -> when (state) {
                                OPEN -> {
                                    secondView.changByState(OPENLEFT)
                                    view.changByState(OPENCENTER)
                                }
                                NORMAL -> {
                                    secondView.changByState(OPEN)
                                    view.changByState(DISABLELEFT)
                                }
                                else -> {
                                    secondView.changByState(OPEN)
                                    view.changByState(NORMAL)
                                }
                            }
                        }
                    }

                    secondView.getClassState() == DISABLE -> {
                        when {
                            thirdView.getClassState() == DISABLE -> when (state) {
                                OPEN -> {
                                    secondView.changByState(DISABLERIGHT)
                                    view.changByState(OPENLEFT)
                                }
                                DISABLE -> {
                                    secondView.changByState(DISABLECENTER)
                                    view.changByState(DISABLECENTER)
                                }
                                else -> {
                                    secondView.changByState(DISABLERIGHT)
                                    view.changByState(NORMAL)
                                }
                            }
                            thirdView.getClassState() == OPEN -> when (state) {
                                OPEN -> {
                                    secondView.changByState(DISABLE)
                                    view.changByState(OPEN)
                                }
                                DISABLE -> {
                                    secondView.changByState(DISABLELEFT)
                                    view.changByState(DISABLECENTER)
                                }
                                else -> {
                                    secondView.changByState(DISABLE)
                                    view.changByState(NORMAL)
                                }
                            }
                            else -> when (state) {
                                OPEN -> {
                                    secondView.changByState(DISABLE)
                                    view.changByState(OPENLEFT)
                                }
                                DISABLE -> {
                                    secondView.changByState(DISABLELEFT)
                                    view.changByState(DISABLECENTER)
                                }
                                else -> {
                                    secondView.changByState(DISABLE)
                                    view.changByState(NORMAL)
                                }
                            }
                        }
                    }
                    else -> {
                        when (state) {
                            OPEN -> view.changByState(OPENLEFT)
                            DISABLE -> view.changByState(DISABLELEFT)
                            else -> view.changByState(NORMAL)
                        }
                    }
                }
            }
        }
    }


    override fun onInvisibleToUser() {

    }


    interface ScheduleCardFragmentCallBack {
        fun scheduleClick(view: ScheduleDateTextView, checked: Boolean, position: Int, list: List<TeacherSchedule>)
    }


    companion object {
        const val ENABLE = 1
        const val EDITDISABLE = 2
        const val EDITDISCOUNT = 3

        @JvmStatic
        fun createInstance(date: String, type: Int, id: Int): ScheduleCardFragment {
            return ScheduleCardFragment().apply {
                arguments = Bundle(3).apply {
                    putString("date", date)
                    putInt("type", type)
                    putInt("id", id)
                }

            }
        }
    }
}
