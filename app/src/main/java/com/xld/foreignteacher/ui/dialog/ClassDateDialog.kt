package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import cn.sinata.xldutils.utils.TimeUtils
import com.aigestudio.wheelpicker.WheelPicker
import com.xld.foreignteacher.R
import java.util.*

/**
 * Created by cz on 5/3/18.
 */
class ClassDateDialog : AppCompatDialogFragment() {
    private lateinit var datePicker: WheelPicker
    private lateinit var hourPicker: WheelPicker
    private lateinit var minPicker: WheelPicker
    private lateinit var listener: ClassDateDialogListener
    private var dateList = mutableListOf<String>()
    private var hourList = mutableListOf<String>()
    private var minList = mutableListOf<String>()
    private var timeList = mutableListOf<Date>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_select_class_date, container, false)
        minPicker = view.findViewById(R.id.loop_data_min)
        hourPicker = view.findViewById(R.id.loop_data_hour)
        datePicker = view.findViewById(R.id.loop_data_month)
        val c = Calendar.getInstance()
        initData(c)
        datePicker.data = dateList
        hourPicker.data = hourList
        minPicker.data = minList
        view.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            val date = datePicker.data[datePicker.currentItemPosition].toString()
            val hour = hourPicker.data[hourPicker.currentItemPosition].toString()
            val min = minPicker.data[minPicker.currentItemPosition].toString()
            val time =timeList[datePicker.currentItemPosition]
            listener.clickSure(date,hour,min,time)
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            listener.clickCancel()
            dismiss()
        }

        return view
    }

    private fun initData(c:Calendar) {

        for (i in 0 until 15) {
            if (i == 0) {
                c.add(Calendar.DAY_OF_MONTH, 0)
            } else {
                c.add(Calendar.DAY_OF_MONTH, 1)
            }
            timeList.add(c.time)
            dateList.add(TimeUtils.getTimeDM(c.time))
        }

        for (i in 1..24) {
            if (i<10){
                hourList.add("0$i")
            }else {
                hourList.add(i.toString())
            }
        }
        for (i in 1..60) {
            if (i<10){
                minList.add("0$i")
            }else {
                minList.add(i.toString())
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.CustomBottomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_select_class_date)
        dialog.setCanceledOnTouchOutside(true)

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        val window = dialog.window
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp

        return dialog
    }

    fun setListener(classDateDialogListener: ClassDateDialogListener): ClassDateDialog {
        listener = classDateDialogListener
        return this
    }

    interface ClassDateDialogListener {
        fun clickCancel()
        fun clickSure(date: String,hour:String,min:String,time:Date)
    }


    class Builder {
        fun create(): ClassDateDialog {
            return ClassDateDialog()
        }
    }

}
