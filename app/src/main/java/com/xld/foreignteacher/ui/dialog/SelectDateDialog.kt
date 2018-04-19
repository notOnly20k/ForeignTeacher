package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import com.aigestudio.wheelpicker.widgets.WheelDayPicker
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker
import com.aigestudio.wheelpicker.widgets.WheelYearPicker
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/19/18.
 */
class SelectDateDialog : AppCompatDialogFragment() {
    private lateinit var yearView:WheelYearPicker
    private lateinit var monthView:WheelMonthPicker
    private lateinit var dayView:WheelDayPicker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_selet_birth, container, false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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
}
