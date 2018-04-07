package com.xld.foreignteacher.ui.userinfo

import com.example.liangmutian.mypicker.DatePickerDialog
import com.example.liangmutian.mypicker.DateUtil
import com.swifty.dragsquareimage.DraggablePresenterImpl
import com.xld.foreignteacher.R
import com.xld.foreignteacher.ui.base.BaseTranslateStatusActivity
import com.xld.foreignteacher.ui.dialog.CustomDialog
import com.xld.foreignteacher.ui.dialog.MyActionDialog
import com.xld.foreignteacher.ui.dialog.StarrBarDialog
import com.xld.foreignteacher.views.StarBarView
import kotlinx.android.synthetic.main.activity_user_info.*

/**
 * Created by cz on 3/29/18.
 */
class EditTeacherInfoActivity : BaseTranslateStatusActivity() {
    override val changeTitleBar: Boolean
        get() = false


    override val contentViewResId: Int
        get() = R.layout.activity_user_info


    override fun initView() {
        val draggablePresenter = DraggablePresenterImpl(this, drag_square)
        draggablePresenter.setCustomActionDialog(MyActionDialog(this))

        star_chinese_level.setOnClickListener {

        }


        tv_gender_edit.setOnClickListener({
            CustomDialog.Builder()
                    .create()
                    .setButton1Text("Male")
                    .setButton2Text("Female")
                    .setDialogListene(object : CustomDialog.CustomDialogListener {
                        override fun clickButton1(customDialog: CustomDialog) {
                            tv_gender_edit.text = "Male"
                            customDialog.dismiss()
                        }

                        override fun clickButton2(customDialog: CustomDialog) {
                            tv_gender_edit.text = "Female"
                            customDialog.dismiss()
                        }


                    })
                    .showtitle(false)
                    .show(supportFragmentManager, "sex_dialog")
        })

        tv_birth_edit.setOnClickListener {
            showDateDialog(DateUtil.getDateForString("1990-01-01"))
        }

        tv_current_city_edit.setOnClickListener {

        }
        tv_language.setOnClickListener {
            activityUtil.go(SelectLanguageActivity::class.java).start()
        }
        tv_chinese_level.setOnClickListener {
            StarrBarDialog().apply {
                setDialogListene(object : StarrBarDialog.SelectStarListener {
                    override fun onOkClick(level: Float) {
                       this@EditTeacherInfoActivity.findViewById<StarBarView>(R.id.star_chinese_level).setStarRating(level)
                    }

                })
            }.show(supportFragmentManager, "star_dialog")
        }

    }

    override fun initData() {

    }

    private fun showDateDialog(date: List<Int>) {
        val builder = DatePickerDialog.Builder(this)
        builder.setOnDateSelectedListener(object : DatePickerDialog.OnDateSelectedListener {
            override fun onDateSelected(dates: IntArray) {

                tv_birth_edit.text = (dates[0].toString() + "-" + (if (dates[1] > 9) dates[1] else "0" + dates[1]) + "-"
                        + if (dates[2] > 9) dates[2] else "0" + dates[2])

            }

            override fun onCancel() {

            }
        })

                .setSelectYear(date[0] - 1)
                .setSelectMonth(date[1] - 1)
                .setSelectDay(date[2] - 1)

        builder.setMaxYear(DateUtil.getYear())
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday())[1])
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday())[2])
        val dateDialog = builder.create()
        dateDialog.show()
    }
}
