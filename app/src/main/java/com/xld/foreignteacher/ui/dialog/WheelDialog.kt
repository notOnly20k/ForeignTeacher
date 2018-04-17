package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import com.aigestudio.wheelpicker.WheelPicker
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/17/18.
 */
class WheelDialog : AppCompatDialogFragment() {
    private lateinit var tvSure: TextView
    private lateinit var tvCancel: TextView
    private lateinit var loopView: WheelPicker
    private lateinit var listener: WheelDialogListener
    private var selectData =""
    private var dateList = mutableListOf<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_wheel, container, false)
        tvSure = view.findViewById(R.id.tv_ok)
        tvCancel = view.findViewById(R.id.tv_cancel)
        loopView = view.findViewById(R.id.loop_data)
        tvSure.setOnClickListener {
            listener.clickSure(selectData)
            dismiss()
        }
        loopView.data = dateList
        loopView.setOnItemSelectedListener { picker, data, position -> selectData =data.toString() }
        tvCancel.setOnClickListener {
            listener.clickCancel()
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.CustomBottomDialog)
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

    fun setDate(list: List<String>): WheelDialog {
        dateList.clear()
        dateList.addAll(list)
        return this
    }

    fun setListener(wheelDialogListener: WheelDialogListener): WheelDialog {
        listener = wheelDialogListener
        return this
    }

    interface WheelDialogListener {
        fun clickCancel()
        fun clickSure(string: String)
    }


    class Builder {
        fun create(): WheelDialog {
            return WheelDialog()
        }
    }
}
