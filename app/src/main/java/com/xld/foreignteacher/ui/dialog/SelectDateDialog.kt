package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import com.aigestudio.wheelpicker.widgets.WheelDatePicker
import com.xld.foreignteacher.R
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/19/18.
 */
class SelectDateDialog : AppCompatDialogFragment() {
    private val logger =LoggerFactory.getLogger("SelectDateDialog")
    private lateinit var tvSure: TextView
    private lateinit var tvCancel: TextView
    private var year: Int = 1995
    private var month: Int = 1
    private var day: Int = 1
    private lateinit var datePicker: WheelDatePicker
    private lateinit var listener: SelectDateDialog.SeletDateDialogListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_selet_birth, container, false)
        tvCancel = view.findViewById(R.id.tv_cancel)
        tvSure = view.findViewById(R.id.tv_ok)
        datePicker = view.findViewById(R.id.wd_date)
        datePicker.setYearAndMonth(year, month)
        datePicker.selectedDay = day
        tvSure.setOnClickListener {
            val month = if (datePicker.currentMonth < 10) {
                "0" + datePicker.currentMonth
            } else {
                datePicker.currentMonth
            }

            val day = if (datePicker.currentDay < 10) {
                "0" + datePicker.currentDay
            } else {
                datePicker.currentDay
            }
            val s = "${datePicker.currentYear}-$month-$day"
            listener.clickSure(s)
            dismiss()
        }
        tvCancel.setOnClickListener {
            dismiss()
        }
        return view
    }

    fun setDate(year: Int, month: Int,day:Int): SelectDateDialog {
        this.year = year
        this.month = month
        this.day = day
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog((activity as Context?)!!, R.style.CustomBottomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wheel)
        dialog.setCanceledOnTouchOutside(true)

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        val window = dialog.window
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp

        return dialog
    }


    fun setListener(wheelDialogListener: SeletDateDialogListener): SelectDateDialog {
        listener = wheelDialogListener
        return this
    }

    interface SeletDateDialogListener {
        fun clickCancel()
        fun clickSure(string: String)
    }

    class Builder {
        fun create(): SelectDateDialog {
            return SelectDateDialog()
        }
    }
}
